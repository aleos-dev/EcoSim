package com.empty.ecosim.model.animals.factory;

import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.AnimalSpecification;
import com.empty.ecosim.model.animals.AnimalType;
import com.empty.ecosim.model.animals.herbivores.*;
import com.empty.ecosim.model.animals.predators.*;
import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.fasterxml.jackson.core.type.TypeReference;

public class SimpleAnimalFactory extends AnimalFactory{
    private static final EntitySpecificationLoader<AnimalType, AnimalSpecification> ANIMAL_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ANIMAL, new TypeReference<>(){}
    );

    public Animal createAnimal(AnimalType animalType) {
        Animal animal = switch (animalType) {
            case WOLF -> new Wolf();
            case BOA -> new Boa();
            case FOX -> new Fox();
            case BEAR -> new Bear();
            case EAGLE -> new Eagle();
            case HORSE -> new Horse();
            case DEER -> new Deer();
            case RABBIT -> new Rabbit();
            case MOUSE -> new Mouse();
            case GOAT -> new Goat();
            case SHEEP -> new Sheep();
            case BOAR -> new Boar();
            case BUFFALO -> new Buffalo();
            case DUCK -> new Duck();
            case CATERPILLAR -> new Caterpillar();
        };

        var spec = ANIMAL_SPECIFICATION.getSpecificationForType(animalType);
        animal.setWeight(spec.weight());
        animal.setSpeed(spec.speed());
        animal.setSatiety(spec.maxSatiety());

        return animal;
    }
}

