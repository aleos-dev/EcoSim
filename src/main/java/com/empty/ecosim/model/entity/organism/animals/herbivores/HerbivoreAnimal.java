package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.Animal;

public abstract class HerbivoreAnimal extends Animal {
    private static final int MAX_OFFSPRING = 2;
    private static final int FERTILE_PERIOD = 4;

    public int getOffspringsNumber() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }

}
