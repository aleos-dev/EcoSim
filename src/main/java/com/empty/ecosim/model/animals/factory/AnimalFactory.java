package com.empty.ecosim.model.animals.factory;

import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.AnimalType;

public abstract class AnimalFactory {
    public abstract Animal createAnimal(AnimalType type);
}
