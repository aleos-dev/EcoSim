package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Boa extends Predator {
    @Override
    public AnimalType getType() {
        return AnimalType.BOA;
    }
}