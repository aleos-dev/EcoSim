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
        if (!isHungry()) return;

        var edibleTypesPresent = filterEdibleTypesInCell(cell);
        if (edibleTypesPresent.isEmpty()) return;

        var targetType = RandomGenerator.getRandomOrganismType(edibleTypesPresent);

        if (cell.hasType(targetType)) {
            tryToConsume(cell, targetType);
        }
    }

    private void tryToConsume(Cell cell, OrganismType targetType) {
        if (!isHuntFailed(targetType)) {
            consume(cell, targetType);
        }
    }

    private void consume(Cell cell, OrganismType animalType) {
        Organism prey = cell.getAliveOrganism(animalType);
        if (prey == null) return;

        prey.markAsDead();
        StatisticsCollector.registerPredationCount(animalType);

        setSatiety(Math.min(getSatiety() + prey.getWeight(), getBaseSpecification().maxSatiety()));
    }

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

    private boolean isHuntFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(getBaseSpecification().getChanceToHunt(targetType));
    }
}
