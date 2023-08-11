package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

import java.util.List;

public abstract class HerbivoreAnimal extends Animal {
    private static final double HUNGER_THRESHOLD = 0.95;
    private static final int MAX_OFFSPRING = 4;
    private static final int FERTILE_PERIOD = 6;

    @Override
    public boolean findFoodAt(Cell cell) {

        if (satiety > baseSpecification.maxSatiety() * HUNGER_THRESHOLD) {
            return false;
        }

        List<Organism> grassList = cell.getResidentsByType(PlantType.GRASS);
        if (grassList == null || grassList.isEmpty()) {
            return false;
        }

        double nutrition = grassList.get(0).getWeight();
        int maxIntake = (int) ((baseSpecification.maxSatiety() - satiety) / nutrition) + 1;

        int numberOfFood = cell.handleConsumptionProcess(maxIntake, PlantType.GRASS);
        // Update the satiety based on the consumed food
        satiety = Math.min((satiety + nutrition * numberOfFood), baseSpecification.maxSatiety());

        return true;
    }

    @Override
    public int maxOffspring() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }
}
