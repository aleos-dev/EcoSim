package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import static com.empty.ecosim.utils.RandomGenerator.getRandomOrganismType;

public abstract class PredatorAnimal extends Animal {
    private static final int MAX_OFFSPRING = 2;
    private static final int FERTILE_PERIOD = 5;

    public void eat(Cell cell) {
        if (!isHungry()) return;

        Organism prey = huntForPreyAt(cell);
        consumeFood(prey);
    }

    private Organism huntForPreyAt(Cell cell) {
        var availablePreyTypes = filterEdibleTypesInCell(cell);
        if (availablePreyTypes.isEmpty()) return null;

        var targetType = getRandomOrganismType(availablePreyTypes);
        return isHuntFailed(targetType) ? null : cell.getAliveOrganism(targetType);
    }


    private void consumeFood(Organism prey) {
        if (prey == null) return;
        prey.markAsDead();
        StatisticsCollector.registerPredationCount(prey.getType());
        setSatiety(Math.min(getSatiety() + prey.getWeight(), getBaseSpecification().maxSatiety()));
    }

    private boolean isHuntFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(getBaseSpecification().getChanceToHunt(targetType));
    }

    @Override
    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

}
