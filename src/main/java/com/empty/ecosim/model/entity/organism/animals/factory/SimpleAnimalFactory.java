package com.empty.ecosim.model.entity.organism.animals.factory;

import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalSpecification;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.herbivores.*;
import com.empty.ecosim.model.entity.organism.animals.predators.*;
import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class SimpleAnimalFactory extends AnimalFactory{
    private static final EntitySpecificationLoader<AnimalType, AnimalSpecification> ANIMALS_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ANIMAL, new TypeReference<>(){}
    );

    private final Map<AnimalType, AnimalCreator> animalCreators = new HashMap<>();

    // Interface for animal creators
    private interface AnimalCreator {
        Animal create();
    }

    public SimpleAnimalFactory() {
        // Register animal creators for each animal type
        registerAnimalCreator(AnimalType.WOLF, Wolf::new);
        registerAnimalCreator(AnimalType.BOA, Boa::new);
        registerAnimalCreator(AnimalType.FOX, Fox::new);
        registerAnimalCreator(AnimalType.BEAR, Bear::new);
        registerAnimalCreator(AnimalType.HORSE, Horse::new);
        registerAnimalCreator(AnimalType.DEER, Deer::new);
        registerAnimalCreator(AnimalType.EAGLE, Eagle::new);
        registerAnimalCreator(AnimalType.RABBIT, Rabbit::new);
        registerAnimalCreator(AnimalType.MOUSE, Mouse::new);
        registerAnimalCreator(AnimalType.GOAT, Goat::new);
        registerAnimalCreator(AnimalType.SHEEP, Sheep::new);
        registerAnimalCreator(AnimalType.BUFFALO, Buffalo::new);
        registerAnimalCreator(AnimalType.BOAR, Boar::new);
        registerAnimalCreator(AnimalType.DUCK, Duck::new);
        registerAnimalCreator(AnimalType.CATERPILLAR, Caterpillar::new);
    }

    private void registerAnimalCreator(AnimalType type, AnimalCreator creator) {
        animalCreators.put(type, creator);
    }

    public Animal create(AnimalType animalType) {
        AnimalCreator creator = animalCreators.get(animalType);
        if (creator == null) {
            throw new IllegalArgumentException("AnimalType " + animalType + " is not supported by this factory.");
        }

        Animal animal = creator.create();

        var spec = ANIMALS_SPECIFICATION.getSpecificationForType(animalType);
        animal.setWeight(spec.weight());
        animal.setSpeed(spec.speed());
        animal.setSatiety(spec.maxSatiety());
        animal.setBaseSpecification(spec);
        animal.setEdibleTypes(ANIMALS_SPECIFICATION.getSpecificationForType(animalType).edibleTypes());

        return animal;
    }
}

