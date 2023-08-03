package com.empty.ecosim.model.animals.herbivores;

import com.empty.ecosim.model.animals.AnimalSpecification;
import com.empty.ecosim.model.animals.AnimalType;
import com.empty.ecosim.model.plants.PlantType;

public class Horse extends Herbivores {
    private static final AnimalSpecification SPECIFICATION = ANIMAL_SPECIFICATION.getSpec(AnimalType.HORSE);

    @Override
    public void eat() {
        System.out.println(SPECIFICATION.edibleTypes().get(PlantType.GRASS));
        System.out.println("satiety = " + SPECIFICATION.maxSatiety());
    }

    @Override
    public void reproduce() {

    }

    @Override
    public void move() {

    }
}
