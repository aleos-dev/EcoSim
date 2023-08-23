package com.empty.ecosim.model.entity.organism;


import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;

/**
 * Represents a super factory responsible for creating organisms of different types.
 * This class encapsulates both animal and plant factories.
 */
public class OrganismSuperFactory {
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;

    public OrganismSuperFactory() {
        this.animalFactory = new SimpleAnimalFactory();
        this.plantFactory = new SimplePlantFactory();
    }


    /**
     * Creates an organism instance based on the specified organism type.
     *
     * @param type The type of organism to be created.
     * @return An instance of the specified organism type.
     * @throws IllegalArgumentException if the provided organism type is not supported.
     */
    public Organism create(OrganismType type) {

        if (type instanceof AnimalType animalType) {
            return animalFactory.create(animalType);
        } else if (type instanceof PlantType plantType) {
            return plantFactory.create(plantType);
        } else throw new IllegalArgumentException("AnimalType " + type + " is not supported by this factory.");

    }
}
