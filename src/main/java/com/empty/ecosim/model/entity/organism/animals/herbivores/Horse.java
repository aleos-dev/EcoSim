package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Horse extends Herbivores {

    @Override
    public AnimalType getType() {
        return AnimalType.HORSE;
    }
}
