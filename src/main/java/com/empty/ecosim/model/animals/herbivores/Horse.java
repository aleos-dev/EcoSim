package com.empty.ecosim.model.animals.herbivores;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.animals.AnimalType;

public class Horse extends Herbivores {

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
        return AnimalType.HORSE;
    }
}
