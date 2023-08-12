package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;

public class Main {


    public static void main(String[] args) throws IllegalArgumentException {

        StatisticsCollector statColl = new StatisticsCollector();
        Territory island = new Island();
        statColl.calculateTerritoryStatistics(island);
        initiateIsland(island);

        for (int i = 0; i < 300; i++) {
            statColl.calculateTerritoryStatistics(island);

            allEat(island);
            allReproduce(island);
            allMove(island);

            System.out.println(statColl);

        }
    }

    public static void allReproduce(Territory island) {
        island.getCells().stream()
                .flatMap(cell -> cell.getResidentsMap().entrySet().stream())
                .forEach(entry -> {
                    OrganismType residentType = entry.getKey();
                    List<Organism> residents = entry.getValue();
                    if (residentType instanceof PlantType) {
                        int a = 8-3;
                    }
                    int maxAvailableCapacity = island.getMaximumCapacityFor(residentType) - residents.size();
                    if (maxAvailableCapacity <= 0 || residents.isEmpty()) {
                        return;
                    }
                    List<Organism> newborns = new ArrayList<>();
                    Organism organism = null;

                    for (Organism resident : residents) {

                        organism = resident;
                        newborns.addAll(organism.reproduce());
                        if (newborns.size() > maxAvailableCapacity) {
                            newborns = newborns.subList(0, maxAvailableCapacity);
                            break;
                        }
                    }

                    residents.addAll(newborns);
                    StatisticsCollector.registerNewbornCount(organism.getType(), newborns.size());
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
                            .forEach(animal -> animal.findFoodAt(cell))
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
