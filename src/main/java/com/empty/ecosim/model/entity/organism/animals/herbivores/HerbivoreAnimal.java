package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class HerbivoreAnimal extends Animal {
    private static final double HUNGER_THRESHOLD = 0.95;
    private static final int MAX_OFFSPRING = 5;
    private static final int FERTILE_PERIOD = 10;

    @Override
    public boolean findFoodAt(Cell cell) {
        if (satiety > baseSpecification.maxSatiety() * HUNGER_THRESHOLD) {
            return false;
        }

        var edibleTypesPresent = new ArrayList<>(cell.getPresentTypes());
        edibleTypesPresent.retainAll(edibleTypes);

        if (edibleTypesPresent.isEmpty()) {
            return false;
        }

        var targetType = RandomGenerator.getRandomOrganismType(edibleTypesPresent);

        if (targetType instanceof PlantType) {
            List<Organism> grassList = cell.getResidentsByType(PlantType.GRASS);
            if (grassList == null || grassList.isEmpty()) {
                return false;
            }

            Organism grass = grassList.get(0);
            double nutrition = grass.getWeight();
            int maxIntake = (int) ((baseSpecification.maxSatiety() - satiety) / nutrition) + 1;

            int numberOfFood = cell.handleConsumptionProcess(maxIntake, PlantType.GRASS);
            StatisticsCollector.registerPredationCount(PlantType.GRASS, numberOfFood);
            satiety = Math.min(satiety + nutrition * numberOfFood, baseSpecification.maxSatiety());

            return true;
        } else {
            if (isHuntFailed(targetType)) {
                return false;
            }

            Organism prey = cell.handlePredationProcess(targetType);
            if (prey == null) {
                return false;
            }

            consume(prey);
            return true;
        }
    }

    @Override
    public int maxOffspring() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }
    protected void consume(Organism food) {
        if (food == null) return;
        satiety = Math.min(satiety + food.getWeight(), baseSpecification.maxSatiety());
    }

    private boolean isHuntFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(baseSpecification.getChanceToHunt(targetType));
    }
}
