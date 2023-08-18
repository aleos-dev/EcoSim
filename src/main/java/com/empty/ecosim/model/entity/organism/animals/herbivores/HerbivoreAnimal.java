package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

public abstract class HerbivoreAnimal extends Animal {
    private static final int MAX_OFFSPRING = 5;
    private static final int FERTILE_PERIOD = 10;


    @Override
    public void eat(Cell cell) {
        spendEnergy();
        if (!isHungry() || !isAlive()) return;

        var edibleTypesPresent = filterEdibleTypesInCell(cell);
        var targetType = RandomGenerator.getRandomOrganismType(edibleTypesPresent);

        if (cell.hasType(targetType)) {
            tryToConsume(cell, targetType);
        }
    }

    private void tryToConsume(Cell cell, OrganismType targetType) {
        if (!isConsumeFailed(targetType)) {
            consumeFood(cell, targetType);
        }
    }

    private void consumeFood(Cell cell, OrganismType animalType) {
        Organism food = cell.extractAnyOrganismByType(animalType);
        if (food == null) return;

        StatisticsCollector.registerPredationCount(animalType);
        StatisticsCollector.decreasePopulationCount(animalType, 1);
        setSatiety(Math.min(getSatiety() + food.getWeight(), getBaseSpecification().maxSatiety()));
    }

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

    private boolean isConsumeFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(getBaseSpecification().getChanceToHunt(targetType));
    }
}
