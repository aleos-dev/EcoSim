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

    public void eat(Organism food) {
        spendEnergy();
        consumeFood(food);
    }

    private void consumeFood(Organism prey) {
        prey.markAsDead();
        StatisticsCollector.registerPredationCount(prey.getType());
        StatisticsCollector.decreasePopulationCount(prey.getType(), 1);
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
