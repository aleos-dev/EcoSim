package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.ArrayList;

import static com.empty.ecosim.utils.RandomGenerator.getRandomOrganismType;

public abstract class PredatorAnimal extends Animal {
    private static final double HUNGER_THRESHOLD = 0.95;
    private static final int MAX_OFFSPRING = 2;
    private static final int FERTILE_PERIOD = 5;
    public void findFoodAt(Cell cell) {
        if (satiety > getBaseSpecification().maxSatiety() * HUNGER_THRESHOLD) {
            return;
        }

        Organism prey = getPreyToHunt(cell);
        if (prey == null) {
            return;
        }

        consume(prey);
    }

    @Override
    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

    @Override
    public int maxOffspring() {
        return MAX_OFFSPRING;
    }

    protected Organism getPreyToHunt(Cell cell) {
        var presentTypes = new ArrayList<>(cell.getPresentTypes());
        presentTypes.retainAll(getEdibleTypes());

        if (presentTypes.isEmpty()) {
            return null;
        }

        var targetType = getRandomOrganismType(presentTypes);
        return isHuntFailed(targetType) ? null : cell.handlePredationProcess(targetType);
    }

    protected void consume(Organism food) {
        if (food == null) return;
        satiety = Math.min(satiety + food.getWeight(), getBaseSpecification().maxSatiety());
    }

    private boolean isHuntFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(getBaseSpecification().getChanceToHunt(targetType));
    }
}
