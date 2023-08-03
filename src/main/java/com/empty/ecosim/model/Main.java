package com.empty.ecosim.model;

import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.AnimalSpecification;
import com.empty.ecosim.model.animals.AnimalType;
import com.empty.ecosim.model.animals.herbivores.Horse;
import com.empty.ecosim.model.animals.predators.Wolf;
import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.Specification;
import com.fasterxml.jackson.core.type.TypeReference;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {
        Specification<AnimalType, AnimalSpecification> spec = new Specification<>(
                ConfigurationManager.ResourceType.ANIMAL, new TypeReference<>(){});

        System.out.println(spec.getSpec(AnimalType.HORSE));

        Wolf wolf = (Wolf) Animal.create(AnimalType.WOLF);
        Horse horse = (Horse) Animal.create(AnimalType.HORSE);

        System.out.println(wolf);
        horse.eat();
        System.out.println();
    }
}
