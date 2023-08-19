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
    public void eat(Organism food) {
        spendEnergy();

        consumeFood(food);
    }

    private void consumeFood(Organism food) {
        food.markAsDead();
        StatisticsCollector.registerPredationCount(food.getType());
        StatisticsCollector.decreasePopulationCount(food.getType(), 1);
        setSatiety(Math.min(getSatiety() + food.getWeight(), getBaseSpecification().maxSatiety()));
    }

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

}
