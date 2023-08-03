package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.animals.herbivores.Horse;
import com.empty.ecosim.model.animals.predators.Wolf;
import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.Specification;
import com.fasterxml.jackson.core.type.TypeReference;


public abstract class Animal {
    protected static final Specification<AnimalType, AnimalSpecification> ANIMAL_SPECIFICATION = new Specification<> (
            ConfigurationManager.ResourceType.ANIMAL, new TypeReference<>(){}
    );
    protected int weight;
    protected int speed;
    protected int satiety;

    public abstract void eat();
    public abstract void reproduce();
    public abstract void move();

    public static Animal create(AnimalType animalType) {
        Animal animal = switch (animalType) {
            case WOLF -> new Wolf();
            case HORSE -> new Horse();
        };

        var spec = ANIMAL_SPECIFICATION.getSpec(animalType);
        animal.weight = spec.weight();
        animal.speed = spec.speed();
        animal.satiety = spec.maxSatiety();

        return animal;
    }

}
