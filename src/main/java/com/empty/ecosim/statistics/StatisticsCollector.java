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
    private static Map<OrganismType, Integer> predationCountCollector = new HashMap<>();
    public static Map<OrganismType, Integer> starvingCountCollector = new HashMap<>();
    private static Map<OrganismType, Integer> newbornCountCollector = new HashMap<>();
    

    private Map<OrganismType, Integer> populationCountMap = new HashMap<>();
    private Map<OrganismType, Integer> predationCountMap = new HashMap<>();
    private Map<OrganismType, Integer> starvingCountMap = new HashMap<>();
    private Map<OrganismType, Integer> newbornCountMap = new HashMap<>();

    private int animalCount;
    private int animalKillCount;
    private int animalNewbornCount;
    private int animalStarvingCount;
    private int plantCount;
    private int plantKillCount;
    private int plantNewbornCount;

    
    public synchronized static void registerPredationCount(OrganismType type) {
        predationCountCollector.merge(type, 1, Integer::sum);
    }

    public synchronized static void registerStarvingCount(OrganismType type) {
        starvingCountCollector.merge(type, 1, Integer::sum);
    }

    public synchronized static void registerNewbornCount(OrganismType type, int numberOfNewborns) {
        newbornCountCollector.merge(type, numberOfNewborns, Integer::sum);
    }


    public void calculateTerritoryStatistics(Territory island) {
        getReady();
        calculatePopulation(island);

        animalCount = calculateNumberOfAnimals();
        animalKillCount = calculateKilledAnimalCount();
        animalNewbornCount = calculateNewbornAnimalCount();
        animalStarvingCount = calculateStarvingAnimalCount();
        plantCount = calculateNumberOfPlants();
        plantKillCount = calculateKilledPlantCount();
        plantNewbornCount = calculateNewbornPlantCount();
    }

    private int calculateNumberOfAnimals() {
        animalCount = populationCountMap.keySet().stream()
                .filter(type -> type instanceof AnimalType)
                .map(populationCountMap::get)
                .mapToInt(Integer::intValue)
                .sum();
        return animalCount;
    }


    private int calculateKilledAnimalCount() {
        return predationCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof AnimalType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private int calculateStarvingAnimalCount() {
        return starvingCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof AnimalType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private int calculateNewbornAnimalCount() {
        return newbornCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof AnimalType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private int calculateNumberOfPlants() {
        plantCount = populationCountMap.keySet().stream()
                .filter(type -> type instanceof PlantType)
                .map(populationCountMap::get)
                .mapToInt(Integer::intValue)
                .sum();
        return plantCount;
    }

    private int calculateKilledPlantCount() {
        return predationCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof PlantType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
    private int calculateNewbornPlantCount() {
        return newbornCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof PlantType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private void getReady() {
        populationCountMap = new HashMap<>();
        dumpCollectors();

    }

    private void dumpCollectors() {
        predationCountMap = predationCountCollector;
        starvingCountMap = starvingCountCollector;
        newbornCountMap = newbornCountCollector;

        predationCountCollector = new HashMap<>();
        starvingCountCollector = new HashMap<>();
        newbornCountCollector = new HashMap<>();
    }


    private void calculatePopulation(Territory island) {
        island.getCells()
                .forEach(cell -> Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
                        .forEach(organismType -> {
                            int residentCount = cell.getResidentCountByType(organismType);
                            populationCountMap.merge(organismType, residentCount, Integer::sum);
                        }));
    }

    private String getOrganismTypeStatistics() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("%-20s %-15s %-15s %-15s %-15s%n",
                "Type", "Population", "Killed", "Starving", "Newborn"));
        result.append("===============================================================================\n");

        Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
                .forEach(type -> {
                    int population = populationCountMap.getOrDefault(type, 0);
                    int newbornCount = newbornCountMap.getOrDefault(type, 0);
                    int predationCount = predationCountMap.getOrDefault(type, 0);
                    int starvingCount = starvingCountMap.getOrDefault(type, 0);
                    result.append(String.format("%-20s %-15d %-15d %-15d %-15d%n",
                            type, population, predationCount, starvingCount, newbornCount));
                });

        result.append("===============================================================================\n");
        return result.toString();
    }

    private String getTotalStatistics() {
        String totalStatisticFormat = "%-20s %-15s %-15d %-15s %-15d%n";
        return String.format(totalStatisticFormat, "Number of Animals:", animalCount, animalKillCount, animalStarvingCount, animalNewbornCount)
                + String.format(totalStatisticFormat, "Number of Plants:", plantCount, plantKillCount, "-", plantNewbornCount);
    }
    

    @Override
    public String toString() {
        return getOrganismTypeStatistics() +
                "===============================================================================\n\n" +
                getTotalStatistics() +
                "===============================================================================\n\n";
    }


}
