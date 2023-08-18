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
    private static final Map<OrganismType, Integer> populationCountCollector = new HashMap<>();
    private static Map<OrganismType, Integer> predationCountCollector = new HashMap<>();
    private static Map<OrganismType, Integer> starvingCountCollector = new HashMap<>();
    private static Map<OrganismType, Integer> newbornCountCollector = new HashMap<>();


    private Map<OrganismType, Integer> populationCountMap = new HashMap<>();
    private Map<OrganismType, Integer> populationCountOldMap = new HashMap<>();
    private Map<OrganismType, Integer> predationCountMap = new HashMap<>();
    private Map<OrganismType, Integer> starvingCountMap = new HashMap<>();
    private Map<OrganismType, Integer> newbornCountMap = new HashMap<>();

    private int animalCount;
    private int animalCountOld;
    private int animalKillCount;
    private int animalNewbornCount;
    private int animalStarvingCount;
    private int plantCount;
    private int plantCountOld;
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

    public synchronized static void increasePopulationCount(OrganismType type, int numberOfPopulation) {
        populationCountCollector.merge(type, numberOfPopulation, Integer::sum);
    }

    public synchronized static void decreasePopulationCount(OrganismType type, int numberOfPopulation) {
       populationCountCollector.merge(type, numberOfPopulation, (a, b) -> a - b);
    }


    public void calculateTerritoryStatistics() {
        dumpCollectors();
//        calculatePopulation(island);

        animalCount = calculateNumberOfAnimals();
        animalKillCount = calculateKilledAnimalCount();
        animalNewbornCount = calculateNewbornAnimalCount();
        animalStarvingCount = calculateStarvingAnimalCount();
        plantCount = calculateNumberOfPlants();
        plantKillCount = calculateKilledPlantCount();
        plantNewbornCount = calculateNewbornPlantCount();
    }

//    private void calculatePopulation(Territory island) {
//        island.getCells()
//                .forEach(cell -> Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
//                        .forEach(organismType -> {
//                            int residentCount = cell.getResidentCountByType(organismType);
//                            populationCountMap.merge(organismType, residentCount, Integer::sum);
//                        }));
//    }

    private int calculateNumberOfAnimals() {
        animalCountOld = animalCount;
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
        plantCountOld = plantCount;
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


    private void dumpCollectors() {
        populationCountOldMap = new HashMap<>(populationCountMap);
        populationCountMap = new HashMap<>(populationCountCollector);
        predationCountMap = predationCountCollector;
        starvingCountMap = starvingCountCollector;
        newbornCountMap = newbornCountCollector;

        predationCountCollector = new HashMap<>();
        starvingCountCollector = new HashMap<>();
        newbornCountCollector = new HashMap<>();

    }

    private String getOrganismTypeStatistics() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("%-20s %-12s %-12s %-15s %-15s %-15s%n",
                "Type", "Start", "End", "Killed", "Starving", "Newborn"));
        result.append("=".repeat(100)).append("\n");

        Stream.concat(Arrays.stream(AnimalType.values()), Arrays.stream(PlantType.values()))
                .forEach(type -> {
                    int populationBefore = populationCountOldMap.getOrDefault(type, 0);
                    int populationAfter = populationCountMap.getOrDefault(type, 0);
                    int newbornCount = newbornCountMap.getOrDefault(type, 0);
                    int predationCount = predationCountMap.getOrDefault(type, 0);
                    int starvingCount = starvingCountMap.getOrDefault(type, 0);
                    result.append(String.format("%-20s %-12d %-12d %-15d %-15d %-15d%n",
                            type, populationBefore, populationAfter, predationCount, starvingCount, newbornCount));
                });

        result.append("=".repeat(100)).append("\n");
        return result.toString();
    }

    private String getTotalStatistics() {
        String totalStatisticFormat = "%-20s %-12s %-12s %-15d %-15s %-15d%n";
        return String.format(totalStatisticFormat, "Number of Animals:", animalCountOld, animalCount, animalKillCount, animalStarvingCount, animalNewbornCount)
                + String.format(totalStatisticFormat, "Number of Plants:",plantCountOld, plantCount, plantKillCount, "-", plantNewbornCount);
    }
    

    @Override
    public String toString() {
        String line = "=".repeat(100) + "\n";
        return getOrganismTypeStatistics() + line + getTotalStatistics() + line;
    }
}
