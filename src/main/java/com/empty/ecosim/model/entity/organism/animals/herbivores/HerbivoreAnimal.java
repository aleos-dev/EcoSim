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
    private static final double HUNGER_THRESHOLD = 0.80;
    private static final int MAX_OFFSPRING = 5;
    private static final int FERTILE_PERIOD = 10;

    @Override
    public void findFoodAt(Cell cell) {
        if (!isHungry()) return;

        var edibleTypesPresent = getEdibleTypesInCell(cell);
        if (edibleTypesPresent.isEmpty()) return;

        var targetType = RandomGenerator.getRandomOrganismType(edibleTypesPresent);

        if (cell.getResidentCountByType(targetType) > 0) {
            consumeFood(cell, targetType);
        }
    }

    private void consumeFood(Cell cell, OrganismType targetType) {
        if (targetType instanceof PlantType plantType) {
            consumePlant(cell, plantType);
        } else if (!isHuntFailed(targetType)) {
            Organism prey = cell.handlePredationProcess(targetType);
            consume(prey);
        }

    }
    private void consumePlant(Cell cell, PlantType plantType) {
        List<Organism> targetList = cell.getResidentsCopyByType(plantType);
        double nutritionalValue = targetList.stream().findFirst().get().getWeight();
        double maxSatiety = getBaseSpecification().maxSatiety();
        int numberToIntakeToGetFull = (int) ((maxSatiety - getSatiety()) / nutritionalValue) + 1;
        int managedToEatNumber = cell.handleConsumptionProcess(numberToIntakeToGetFull, plantType);
        setSatiety(Math.min(getSatiety() + nutritionalValue * managedToEatNumber, maxSatiety));

        StatisticsCollector.registerPredationCount(plantType, managedToEatNumber);
    }

    protected void consume(Organism food) {
        if (food == null) return;
        setSatiety(Math.min(getSatiety() + food.getWeight(), getBaseSpecification().maxSatiety()));
    }

    private boolean isHungry() {
        return satiety <= getBaseSpecification().maxSatiety() * HUNGER_THRESHOLD;
    }

    private List<OrganismType> getEdibleTypesInCell(Cell cell) {
        var edibleTypesPresent = new ArrayList<>(cell.getPresentTypes());
        edibleTypesPresent.retainAll(edibleTypes);
        return edibleTypesPresent;
    }

    @Override
    public int maxOffspring() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }


    private boolean isHuntFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(getBaseSpecification().getChanceToHunt(targetType));
    }
}
