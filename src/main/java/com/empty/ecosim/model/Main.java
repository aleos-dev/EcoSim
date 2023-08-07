package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.predators.Wolf;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {


        Territory island = new Island();
        initiateIsland(island);
        Wolf wolf = new Wolf();
        island.getCells().get(0).addResident(wolf);
        System.out.println(island);
        for (int i = 0; i < 10; i++) {
            allMove(island);
            System.out.println(wolf);
        }
    }

    public static void allMove(Territory island) {
        island.getCells().forEach(cell -> {
            List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
            animalTypes.forEach(type -> {
                List<Organism> list = cell.getResidentListIfPresent(type);
                if (list != null) {
                    for (int i = list.size() - 1; i >= 0; i--) {
                        Organism o = list.get(i);
                        if (o instanceof Animal animal) {
                            animal.move(island, cell);
                        }
                    }
                }
            });
        });
    }

    public static void initiateIsland(Territory island) {
        AnimalFactory animalFactory = new SimpleAnimalFactory();
        PlantFactory plantFactory = new SimplePlantFactory();

        List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
        List<PlantType> plantTypes = Arrays.asList(PlantType.values());
        island.getCells().stream().forEach(cell -> {
            Collections.shuffle(animalTypes);
            int varietyTypeNumber = RandomGenerator.getRandomInt(animalTypes.size() + 1);

            animalTypes.stream().limit(varietyTypeNumber).forEach(type -> {
                int typeNumber = RandomGenerator.getRandomInt(island.getMaxResidentNumber(type));
                for (int i = 0; i < typeNumber; i++) {
                    cell.addResident(animalFactory.createAnimal(type));
                }
            });

            varietyTypeNumber = plantTypes.size();
            plantTypes.stream().forEach(type -> {
                int typeNumber = RandomGenerator.getRandomInt(island.getMaxResidentNumber(type));
                for (int i = 0; i < typeNumber; i++) {
                    cell.addResident(plantFactory.createPlant(type));
                }
            });

        });

    }
}
