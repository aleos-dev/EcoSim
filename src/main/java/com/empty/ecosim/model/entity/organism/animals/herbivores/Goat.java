package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Goat extends Animal {

    @Override
    public AnimalType getType() {
        return AnimalType.GOAT;
    }
}
