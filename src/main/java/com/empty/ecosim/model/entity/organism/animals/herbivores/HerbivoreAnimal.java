package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.statistics.StatisticsCollector;

public abstract class HerbivoreAnimal extends Animal {
    private static final int MAX_OFFSPRING = 1;
    private static final int FERTILE_PERIOD = 2;


    @Override
    public void eat(Organism food) {
        depleteSatiety();

        consumeFood(food);
    }

    private void consumeFood(Organism food) {
        food.die();
        StatisticsCollector.registerPredationCount(food.getType());
        setSatiety(Math.min(getSatiety() + food.getWeight(), getBaseSpecification().maxSatiety()));
    }

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

}
