package com.empty.ecosim.model.animals.predators;

import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.AnimalSpecification;
import com.empty.ecosim.model.animals.AnimalType;

public class Wolf extends Animal {
    private static final AnimalSpecification SPECIFICATION = ANIMAL_SPECIFICATION.getSpec(AnimalType.WOLF);

    @Override
    public void eat() {
    }

    @Override
    public void reproduce() {

    }

    @Override
    public void move() {

    }
}
