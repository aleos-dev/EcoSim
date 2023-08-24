package com.empty.ecosim.statistics;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Collects and calculates various statistics related to organisms in an ecosystem.
 */
public class StatisticsCollector implements Runnable {
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

    private int overallAnimalCount;
    private int overallAnimalKillCount;
    private int overallAnimalStarvingCount;
    private int overallPlantCount;
    private int overallPlantKillCount;

    /**
     * Registers a predation count for a given organism type.
     *
     * @param type The type of organism that was predated.
     */
    public synchronized static void registerPredationCount(OrganismType type) {
        predationCountCollector.merge(type, 1, Integer::sum);
        decreasePopulationCount(type, 1);
    }

    /**
     * Registers a starving count for a given organism type.
     *
     * @param type The type of organism that starved.
     */
    public synchronized static void registerStarvingCount(OrganismType type) {
        starvingCountCollector.merge(type, 1, Integer::sum);
        decreasePopulationCount(type, 1);
    }

    /**
     * Registers a newborn count for a given organism type.
     *
     * @param type             The type of organism that was born.
     * @param numberOfNewborns The count of newborn organisms of that type.
     */
    public synchronized static void registerNewbornCount(OrganismType type, int numberOfNewborns) {
        newbornCountCollector.merge(type, numberOfNewborns, Integer::sum);
        increasePopulationCount(type, numberOfNewborns);
    }

    /**
     * Resets the newborn count to its default state.
     */
    public synchronized static void resetNewbornCount() {
        newbornCountCollector = new HashMap<>();
    }

    /**
     * Increases the population count for a given organism type.
     *
     * @param type               The type of organism.
     * @param numberOfPopulation The count by which to increase the population.
     */
    public synchronized static void increasePopulationCount(OrganismType type, int numberOfPopulation) {
        populationCountCollector.merge(type, numberOfPopulation, Integer::sum);
    }

    /**
     * Decreases the population count for a given organism type.
     *
     * @param type               The type of organism.
     * @param numberOfPopulation The count by which to decrease the population.
     */
    public synchronized static void decreasePopulationCount(OrganismType type, int numberOfPopulation) {
        populationCountCollector.merge(type, numberOfPopulation, (a, b) -> a - b);
    }

    @Override
    public void run() {
        try {
            calculateTerritoryStatistics();
            System.out.println(this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates statistics for organisms in the territory.
     */
    public void calculateTerritoryStatistics() {
        dumpCollectors();

        animalCount = calculateNumberOfAnimals();
        animalKillCount = calculateKilledAnimalCount();
        animalNewbornCount = calculateNewbornAnimalCount();
        animalStarvingCount = calculateStarvingAnimalCount();
        plantCount = calculateNumberOfPlants();
        plantKillCount = calculateKilledPlantCount();
        plantNewbornCount = calculateNewbornPlantCount();

        overallAnimalCount += animalNewbornCount;
        overallAnimalKillCount += animalKillCount;
        overallAnimalStarvingCount += animalStarvingCount;
        overallPlantCount += plantNewbornCount;
        overallPlantKillCount += plantKillCount;
    }

    /**
     * Transfers statistics from the collectors to the local maps and resets the collectors.
     * This ensures that data is saved and the collectors are ready to collect new data.
     */
    private void dumpCollectors() {
        populationCountOldMap = new HashMap<>(populationCountMap);

        synchronized (StatisticsCollector.class) {
            populationCountMap = new HashMap<>(populationCountCollector);
            predationCountMap = predationCountCollector;
            starvingCountMap = starvingCountCollector;
            newbornCountMap = newbornCountCollector;

            predationCountCollector = new HashMap<>();
            starvingCountCollector = new HashMap<>();
            newbornCountCollector = new HashMap<>();
        }
    }

    /**
     * Calculates the total number of animals in the current territory.
     *
     * @return the total count of animals.
     */
    private int calculateNumberOfAnimals() {
        animalCountOld = animalCount;
        animalCount = populationCountMap.keySet().stream()
                .filter(type -> type instanceof AnimalType)
                .map(populationCountMap::get)
                .mapToInt(Integer::intValue)
                .sum();
        return animalCount;
    }


    /**
     * Calculates the total number of animals that have been killed.
     *
     * @return the total count of killed animals.
     */
    private int calculateKilledAnimalCount() {
        return predationCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof AnimalType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    /**
     * Calculates the total number of animals that have starved.
     *
     * @return the total count of starving animals.
     */
    private int calculateStarvingAnimalCount() {
        return starvingCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof AnimalType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    /**
     * Calculates the total number of newborn animals.
     *
     * @return the total count of newborn animals.
     */
    private int calculateNewbornAnimalCount() {
        return newbornCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof AnimalType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    /**
     * Calculates the total number of plants in the current territory.
     *
     * @return the total count of plants.
     */
    private int calculateNumberOfPlants() {
        plantCountOld = plantCount;
        plantCount = populationCountMap.keySet().stream()
                .filter(type -> type instanceof PlantType)
                .map(populationCountMap::get)
                .mapToInt(Integer::intValue)
                .sum();
        return plantCount;
    }

    /**
     * Calculates the total number of plants that have been killed.
     *
     * @return the total count of killed plants.
     */
    private int calculateKilledPlantCount() {
        return predationCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof PlantType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    /**
     * Calculates the total number of newborn plants.
     *
     * @return the total count of newborn plants.
     */
    private int calculateNewbornPlantCount() {
        return newbornCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof PlantType)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }


    /**
     * Generates a formatted string representing the statistics of each organism type.
     *
     * @return the string representation of organism type statistics.
     */
    private String getOrganismTypeStatistics() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("%-20s %-12s %-12s %-15s %-15s %-15s%n",
                "Organism Type", "Initial", "Final", "Predation", "Starvation", "Birth"));
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

    /**
     * Generates a formatted string representing the total statistics of animals and plants.
     *
     * @return the string representation of total statistics.
     */
    private String getTotalStatistics() {
        String totalStatisticFormat = "%-20s %-12s %-12s %-15d %-15s %-15d%n";
        return String.format(totalStatisticFormat, "Number of Animals:", animalCountOld, animalCount, animalKillCount, animalStarvingCount, animalNewbornCount)
                + String.format(totalStatisticFormat, "Number of Plants:", plantCountOld, plantCount, plantKillCount, "-", plantNewbornCount);
    }

    /**
     * Generates a formatted string representing the overall statistics of animals and plants.
     *
     * @return the string representation of overall statistics.
     */
    private String getOverallStatistics() {
        String overallStatisticFormat = "%-20s %-12s %-12s %-15s%n";

        return String.format(overallStatisticFormat,
                "Overall", "Count", "Killed", "Starving") +
                String.format(overallStatisticFormat, "Animals:"
                        , overallAnimalCount, overallAnimalKillCount, overallAnimalStarvingCount) +
                String.format(overallStatisticFormat, "Plants:"
                        , overallPlantCount, overallPlantKillCount, "-");
    }

    /**
     * Overrides the {@code toString} method to provide a comprehensive representation
     * of the territory statistics.
     *
     * @return a string representation of the territory statistics.
     */
    @Override
    public String toString() {
        String line = "=".repeat(100) + "\n";
        return getOrganismTypeStatistics() + line + getTotalStatistics() + line + getOverallStatistics() + line;
    }
}
