package com.empty.ecosim.model.animals.predators;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.animals.AnimalType;

public class Wolf extends Predator {

    @Override
    public void eat(Entity entity) {

    }

    @Override
    public void reproduce() {

    }

    @Override
    public void move() {

    }
    @Override
    public AnimalType getType() {
        return AnimalType.WOLF;
    }
}
