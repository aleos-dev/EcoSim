package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Boa extends PredatorAnimal {
    private static final int MAX_OFFSPRING = 2;
    private static final int FERTILE_PERIOD = 5;
  @Override
    public AnimalType getType() {
        return AnimalType.BOA;
    }
    @Override
    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }
    @Override
    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }
}
