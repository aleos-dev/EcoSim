package com.empty.ecosim.model.animals.herbivores;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.AnimalType;

public class Caterpillar extends Animal {

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
        return AnimalType.CATERPILLAR;
    }
}
