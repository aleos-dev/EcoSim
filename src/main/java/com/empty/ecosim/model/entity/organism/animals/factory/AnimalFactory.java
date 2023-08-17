package com.empty.ecosim.model.entity.organism.animals.factory;

import com.empty.ecosim.model.entity.organism.OrganismSuperFactory;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public abstract class AnimalFactory {
    public abstract Animal create(AnimalType type);
}
