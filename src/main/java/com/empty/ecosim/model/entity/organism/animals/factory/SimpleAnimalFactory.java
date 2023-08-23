package com.empty.ecosim.model.entity.organism.animals.factory;

import com.empty.ecosim.model.configuration.userSetup.UserSetupManager;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalSpecification;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.herbivores.*;
import com.empty.ecosim.model.entity.organism.animals.predators.*;
import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a factory for creating simple animals.
 */
public class SimpleAnimalFactory implements AnimalFactory {
    private static final EntitySpecificationLoader<AnimalType, AnimalSpecification> ANIMALS_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ANIMAL, new TypeReference<>() {
    }
    );

    private static final Map<AnimalType, AnimalCreator> ANIMAL_CREATORS = new HashMap<>();

    /**
     * Interface defining a method to create an animal.
     */
    private interface AnimalCreator {
        Animal create();
    }

    /**
     * Constructs a new instance of SimpleAnimalFactory and initializes the animal creators.
     */
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

        applyUserSetup();
    }

    private void registerAnimalCreator(AnimalType type, AnimalCreator creator) {
        ANIMAL_CREATORS.put(type, creator);
    }

    /**
     * Creates an instance of a specific animal type.
     *
     * @param animalType the type of the animal to be created.
     * @return the created animal.
     * @throws IllegalArgumentException if the animal type is not supported by this factory.
     */
    public Animal create(AnimalType animalType) {
        AnimalCreator creator = ANIMAL_CREATORS.get(animalType);
        if (creator == null) {
            throw new IllegalArgumentException("AnimalType " + animalType + " is not supported by this factory.");
        }

        Animal animal = creator.create();

        var spec = ANIMALS_SPECIFICATION.getSpecificationForType(animalType);
        animal.setWeight(spec.weight());
        animal.setSpeed(spec.speed());
        animal.setSatiety(spec.maxSatiety() / 2);
        animal.setGender(RandomGenerator.generateGender());
        animal.setBaseSpecification(spec);
        animal.setEdibleTypes(ANIMALS_SPECIFICATION.getSpecificationForType(animalType).edibleTypes());

        StatisticsCollector.registerNewbornCount(animalType, 1);
        return animal;
    }

    /**
     * Applies user setup to the predefined specifications for the animals.
     */
    public static void applyUserSetup() {
        for (Map.Entry<OrganismType, Integer> entry : UserSetupManager.INSTANCE.get().animalTypeFertilityPeriod().entrySet()) {
            if (entry.getKey() instanceof AnimalType animalType) {
                Animal animal = ANIMAL_CREATORS.get(animalType).create();
                animal.setFertilePeriod(entry.getValue());
            }
        }

        for (Map.Entry<OrganismType, Integer> entry : UserSetupManager.INSTANCE.get().animalTypeOffspringCount().entrySet()) {
            if (entry.getKey() instanceof AnimalType animalType) {
                Animal animal = ANIMAL_CREATORS.get(animalType).create();
                animal.setOffspringCount(entry.getValue());
            }
        }
    }
}

