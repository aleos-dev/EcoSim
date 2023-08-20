package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.statistics.StatisticsCollector;

public abstract class PredatorAnimal extends Animal {
    private static final int MAX_OFFSPRING = 2;
    private static final int FERTILE_PERIOD = 5;

    public void eat(Organism food) {
        depleteSatiety();
        consumeFood(food);
    }

    private void consumeFood(Organism prey) {
        prey.die();
        StatisticsCollector.registerPredationCount(prey.getType());
        setSatiety(Math.min(getSatiety() + prey.getWeight(), getBaseSpecification().maxSatiety()));
    }


    @Override
    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

}
