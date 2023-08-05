package com.empty.ecosim.model.animals.predators;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.animals.AnimalType;

public class Bear extends Predator {

    @Override
    public void eat(Entity entity) {
    }

    @Override
    public void reproduce() {

    }

    @Override
    public Direction move() {

    }

    @Override
    public AnimalType getType() {
        return AnimalType.BEAR;
    }
}
