package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Bear extends PredatorAnimal {

    @Override
    public AnimalType getType() {
        return AnimalType.BEAR;
    }
}
