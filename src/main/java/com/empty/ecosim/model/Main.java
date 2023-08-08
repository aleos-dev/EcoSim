package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.predators.Wolf;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;
import com.empty.ecosim.statistics.IslandStatistic;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {


        Territory island = new Island();
        initiateIsland(island);

        for (int i = 0; i < 30; i++) {
            IslandStatistic stat = new IslandStatistic();
            System.out.println(stat.calculateIslandStatistic(island));
            System.out.println("Animals: " + stat.getNumberOfAnimals());
            System.out.println("Plants: " + stat.getNumberOfPlants());
            allMove(island);
            allEat(island);
            clear(island);
        }
    }

    public static void clear(Territory island) {
        island.getCells().forEach(cell -> {
            Arrays.stream(AnimalType.values())
                    .map(cell::getResidentsByType)
                    .filter(Objects::nonNull)
                    .forEach(list -> list.stream()
                            .map(o -> (Animal) o)
                            .forEach(animal -> {
                                if (!animal.isAlive()) {
                                    cell.removeResidentFromCell(animal);
                                }
                            })
                    );
        });

    }

    public static void allMove(Territory island) {
        island.getCells().forEach(cell -> {
            Arrays.stream(AnimalType.values())
                    .map(cell::getResidentsByType)
                    .filter(Objects::nonNull)
                    .forEach(list -> list.stream()
                            .filter(o -> o instanceof Animal)
                            .map(o -> (Animal) o)
                            .forEach(animal -> animal.move(island, cell))
                    );
        });
    }

    public static void allEat(Territory island) {
        island.getCells().forEach(cell -> {
            Arrays.stream(AnimalType.values())
                    .map(cell::getResidentsByType)
                    .filter(Objects::nonNull)
                    .forEach(list -> list.stream()
                            .filter(o -> o instanceof Animal)
                            .map(o -> (Animal) o)
                            .forEach(animal -> animal.tryToFindFoodAround(cell))
                    );
        });
    }

    public static void initiateIsland(Territory island) {
        AnimalFactory animalFactory = new SimpleAnimalFactory();
        PlantFactory plantFactory = new SimplePlantFactory();

        List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
        List<PlantType> plantTypes = Arrays.asList(PlantType.values());

        island.getCells().forEach(cell -> {

            Collections.shuffle(animalTypes);
            int varietyAnimalTypes = RandomGenerator.getRandomInt(animalTypes.size() + 1);
            animalTypes.stream().limit(varietyAnimalTypes).forEach(animalType -> {
                int animalsNumber = RandomGenerator.getRandomInt(island.getMaxResidentCountForOrganismType(animalType));
                for (int i = 0; i < animalsNumber; i++) {
                    cell.addResident(animalFactory.create(animalType));
                }
            });

            Collections.shuffle(plantTypes);
            int varietyPlantTypes = RandomGenerator.getRandomInt(plantTypes.size() + 1);
            plantTypes.stream().limit(varietyPlantTypes).forEach(plantType -> {
                int plantsNumber = RandomGenerator.getRandomInt(island.getMaxResidentCountForOrganismType(plantType));
                for (int i = 0; i < plantsNumber; i++) {
                    cell.addResident(plantFactory.create(plantType));
                }
            });

        });
    }

}
