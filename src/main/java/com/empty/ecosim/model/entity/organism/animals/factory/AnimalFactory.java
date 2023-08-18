package com.empty.ecosim.model.entity.organism.animals.factory;

import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public interface AnimalFactory {
    Animal create(AnimalType type);
}
