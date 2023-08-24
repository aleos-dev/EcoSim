package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.configuration.userSetup.UserSetupManager;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.Plant;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller responsible for managing the reproduction of organisms
 * within a given territory. This includes both animals and plants.
 */
public class ReproduceController {
    private final Territory territory;
    private final PlantFactory factory = new SimplePlantFactory();

    /**
     * Creates a new instance of the ReproduceController for a given territory.
     *
     * @param territory the territory in which organisms will reproduce.
     */
    public ReproduceController(Territory territory) {
        this.territory = territory;
    }

    /**
     * Executes the reproduction cycle for animals in the territory.
     */
    public void executeReproductionForAnimals() {
        territory.getCells().forEach(this::reproduceAnimalsInCell);
    }

    /**
     * Executes the growth cycle for plants in the territory.
     */
    public void runPlantsGrowth() {
        try {
            territory.getCells().parallelStream().forEach(this::growPlantsInCell);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles reproduction of animals within a specific cell.
     *
     * @param cell the cell to reproduce animals in.
     */
    private void reproduceAnimalsInCell(Cell cell) {
        cell.lock();
        cell.getResidentsMap().entrySet().stream()
                .filter(this::isAnimalsCollection)
                .forEach(this::generateNewborns);
        cell.unlock();
    }

    /**
     * Handles growth of plants within a specific cell.
     *
     * @param cell the cell to grow plants in.
     */
    private void growPlantsInCell(Cell cell) {
        cell.lock();
        Arrays.stream(PlantType.values())
                .forEach(type -> plantGrowth(type, cell));
        cell.unlock();
    }

    /**
     * Executes growth logic for a specific plant type within a cell.
     *
     * @param type the type of plant to grow.
     * @param cell the cell to grow the plant in.
     */
    private void plantGrowth(PlantType type, Cell cell) {
        int growthNumber = calculateGrowthNumber(type, cell);
        List<Plant> plants = generatePlants(type, growthNumber);
        addPlantsToCell(type, cell, plants);
    }

    /**
     * Calculates the number of plants that should grow for a specific plant type in a cell.
     * The calculation is based on available space and a growth threshold.
     *
     * @param type the plant type to calculate growth for.
     * @param cell the cell in which growth is being calculated.
     * @return the number of plants that should grow.
     */
    private int calculateGrowthNumber(PlantType type, Cell cell) {
        int availableCellSpace = getMaxAvailableCapacityFor(type, cell.getOrganismsByType(type));
        return (int) (RandomGenerator.nextInt(0, availableCellSpace) * UserSetupManager.INSTANCE.get().plantGrowthThreshold());
    }

    /**
     * Generates a list of plants of a specified type.
     *
     * @param type the type of plant to be generated.
     * @param growthNumber the number of plants to generate.
     * @return a list of generated plants.
     */
    private List<Plant> generatePlants(PlantType type, int growthNumber) {
        return Stream.generate(() -> factory.create(type))
                .limit(growthNumber)
                .collect(Collectors.toList());
    }

    /**
     * Adds a list of plants to a cell.
     *
     * @param type the type of plant to be added.
     * @param cell the cell to which plants will be added.
     * @param plants the list of plants to add.
     */
    private void addPlantsToCell(PlantType type, Cell cell, List<Plant> plants) {
        cell.getResidentsMap().compute(type, (key, existingSet) -> {
            if (existingSet == null) {
                existingSet = new LinkedHashSet<>();
            }
            existingSet.addAll(plants);
            return existingSet;
        });
    }

    /**
     * Generates offspring for organisms in a cell based on their reproduction logic.
     * The offspring are then added to the resident set.
     *
     * @param entry the set of organisms for which newborns will be generated.
     */
    private void generateNewborns(Map.Entry<OrganismType, Set<Organism>> entry) {
        var residentType = entry.getKey();
        var residentSet = entry.getValue();

        Set<Organism> offspring = residentSet.stream()
                .flatMap(organism -> organism.reproduce().stream())
                .limit(getMaxAvailableCapacityFor(residentType, residentSet))
                .collect(Collectors.toSet());

        residentSet.addAll(offspring);

        StatisticsCollector.registerNewbornCount(residentType, offspring.size());
    }

    /**
     * Determines if the collection represents animals.
     *
     * @param entry the collection to be checked.
     * @return true if the collection represents animals, false otherwise.
     */
    private boolean isAnimalsCollection(Map.Entry<OrganismType, Set<Organism>> entry) {
        return entry.getKey() instanceof AnimalType;
    }

    /**
     * Calculates the maximum available capacity for a specific organism type.
     *
     * @param organismType the organism type to calculate for.
     * @param residents current residing organisms of the type.
     * @return the maximum available capacity for the organism type.
     */
    private int getMaxAvailableCapacityFor(OrganismType organismType, Set<Organism> residents) {
        return Math.max(territory.getMaximumCapacityFor(organismType) - residents.size(), 0);
    }
}

