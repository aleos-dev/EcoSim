package com.empty.ecosim.statistics;

import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StatisticsCollector {
    private final Map<OrganismType, Integer> organismTypePopulation = new HashMap<>();
    private int numberOfAnimals;
    private int numberOfPlants;
    private static int predationProcessCollector;
    private static int starvingProcessCollector;
    private static Map<OrganismType, Integer> newbornStatistic = new HashMap<>();

    public Map<OrganismType, Integer> getOrganismTypePopulation() {
        return organismTypePopulation;
    }

    public static int getAndResetPredationProcessCollector() {
        int result = predationProcessCollector;
        predationProcessCollector = 0;
        return result;
    }

    public static int getAndResetStarvingProcessCollector() {
        int result = starvingProcessCollector;
        starvingProcessCollector = 0;
        return result;
    }

    public static Map<OrganismType, Integer> getAndResetNewbornStatistic() {
        var result = newbornStatistic;
        newbornStatistic = new HashMap<>();
        return result;
    }



    public void calculateTerritoryStatistic(Territory island) {
        initOrganismTypePopulationMap();

        island.getCells()
                .forEach(cell -> {
                    Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
                            .forEach(type -> {
                                organismTypePopulation.compute(type, (key, value) -> value + cell.getResidentCountByType(type));
                            });
                });
    }

    public static void registerPredationProcess(int numberOfPredation) {
        predationProcessCollector += numberOfPredation;
    }

    public static void registerStarvingProcess(int numberOfStarving) {
        starvingProcessCollector += numberOfStarving;
    }


    private void initOrganismTypePopulationMap() {
        Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
                .forEach(type -> organismTypePopulation.put(type, 0));
    }

    public int getNumberOfAnimals() {
        numberOfAnimals = organismTypePopulation.keySet().stream()
                .filter(type -> type instanceof AnimalType)
                .map(organismTypePopulation::get)
                .mapToInt(Integer::intValue)
                .sum();
        return numberOfAnimals;
    }

    public long getNumberOfPlants() {
        numberOfPlants = organismTypePopulation.keySet().stream()
                .filter(type -> type instanceof PlantType)
                .map(organismTypePopulation::get)
                .mapToInt(Integer::intValue)
                .sum();
        return numberOfPlants;
    }


    public static void addNewbornStatistic(OrganismType type, int numberOfNewborns) {
        newbornStatistic.compute(type, (k, v) -> (v == null) ? numberOfNewborns : v + numberOfNewborns);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
                .forEach(type -> {
                    int population = organismTypePopulation.getOrDefault(type, 0);
                    result.append(String.format("Type: %s, population: %d%n", type, population));
                });

        result.append(String.format("Number of Animals: %d%n", getNumberOfAnimals()));
        result.append(String.format("Number of Plants: %d%n", getNumberOfPlants()));
        result.append(String.format("Predation Process Collector: %d%n", getAndResetPredationProcessCollector()));
        result.append(String.format("Starving Process Collector: %d%n", getAndResetStarvingProcessCollector()));

        getAndResetNewbornStatistic().forEach((type, count) -> {
            result.append(String.format("Newborns of Type %s: %d%n", type, count));
        });

        return result.toString();
    }
}
