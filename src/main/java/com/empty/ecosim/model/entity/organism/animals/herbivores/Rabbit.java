package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Rabbit extends HerbivoreAnimal {

    @Override
    public AnimalType getType() {
        return AnimalType.RABBIT;
    }
}
