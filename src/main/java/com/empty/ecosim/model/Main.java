package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.predators.Wolf;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;
import com.empty.ecosim.statistics.IslandStatistic;
import com.empty.ecosim.statistics.OrganismStatistic;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;
import java.util.stream.Stream;

public class Main {

    static OrganismStatistic organismStatistic = new OrganismStatistic();

    public static void main(String[] args) throws IllegalArgumentException {


        Territory island = new Island();
        initiateIsland(island);

        for (int i = 0; i < 30; i++) {
            IslandStatistic stat = new IslandStatistic();
            IslandStatistic stat2 = new IslandStatistic();
            organismStatistic = new OrganismStatistic();
            System.out.println("Stat: " + stat.calculateIslandStatistic(island));
            System.out.println("Stat2: " +stat2.calculateIslandStatistic(island));
            System.out.println("Animals: " + stat.getNumberOfAnimals() + " - " + stat2.getNumberOfAnimals());
            System.out.println("Plants: " + stat.getNumberOfPlants() + " - " + stat2.getNumberOfPlants());
            allEat(island);

            stat = new IslandStatistic();
            System.out.println("AFTER EAT Stat: " + stat.calculateIslandStatistic(island));
            allReproduce(island);


            stat = new IslandStatistic();
            System.out.println("AFTER REPRODUCE Stat: " + stat.calculateIslandStatistic(island));
            allMove(island);

            stat = new IslandStatistic();
            System.out.println("AFTER MOVE Stat: " + stat.calculateIslandStatistic(island));

            System.out.println("Became Food: " + stat.getBecameFood());
            System.out.println("Died of hunger: " + stat.getDieOfHunger());
            System.out.println("newborns: " + organismStatistic);
        }
    }

    public static void allReproduce(Territory island) {
        island.getCells().stream()
                .flatMap(cell -> cell.getResidentsMap().entrySet().stream())
                .forEach(entry -> {
                    OrganismType k = entry.getKey();
                    List<Organism> residents = entry.getValue();

                    List<Organism> newborns = new ArrayList<>();
                    Organism organism = null;
                    for (int i = 0; i < residents.size(); i++) {
                        organism = residents.get(i);
                        newborns.addAll(organism.reproduce());
                    }
                    if (organism == null) return;
                    residents.addAll(newborns);
                    organismStatistic.addNewbornStatistic(organism.getType(), newborns.size());
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
        island.finishTravel();
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
