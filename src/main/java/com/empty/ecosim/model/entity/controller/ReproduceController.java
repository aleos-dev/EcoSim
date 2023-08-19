package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.Reproducible;
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


public class ReproduceController {
    private final Territory territory;

    public ReproduceController(Territory territory) {
        this.territory = territory;
    }

    public void executeReproductionForAnimals() {
        territory.getCells().forEach(this::runReproduceForAnimalsAt);
    }

    public void runPlantsGrowth() {
        try {
//            territory.getCells().parallelStream().forEach(this::runPlantsGrowthForCell);
            territory.getCells().forEach(this::runPlantsGrowthForCell);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void runReproduceForAnimalsAt(Cell cell) {

        cell.lock();
        cell.getResidentsMap().entrySet().stream()
                .filter(this::isAnimalsCollection)
                .filter(this::isReproducible)
                .forEach(this::generateNewborns);

        cell.unlock();
    }


    private final PlantFactory factory = new SimplePlantFactory();

    private void runPlantsGrowthForCell(Cell cell) {

        Arrays.stream(PlantType.values()).forEach(type -> {
            List<Plant> plants = Stream.generate(() -> factory.create(type))
                    .limit(RandomGenerator.getIntRange(0, territory.getMaxResidentCountForOrganismType(type) - cell.getResidentCountByType(type)))
                    .toList();

            cell.getResidentsMap().get(type).addAll(plants);
        });

    }

    private void generateNewborns(Map.Entry<OrganismType, Set<Organism>> entry) {
        var residentType = entry.getKey();
        var residentSet = entry.getValue();
        int maxCapacity = Math.max(territory.getMaximumCapacityFor(residentType) - residentSet.size(), 0);

        Set<Organism> offspring = residentSet.stream()
                .flatMap(organism -> organism.reproduce().stream())
                .limit(maxCapacity)
                .collect(Collectors.toSet());

        entry.getValue().addAll(offspring);

        StatisticsCollector.registerNewbornCount(residentType, offspring.size());
        StatisticsCollector.increasePopulationCount(residentType, offspring.size());
    }

    private boolean isAnimalsCollection(Map.Entry<OrganismType, Set<Organism>> entry) {
        return entry.getKey() instanceof AnimalType;
    }

    private boolean isPlantsCollection(Map.Entry<OrganismType, Set<Organism>> entry) {
        return entry.getKey() instanceof PlantType;
    }

    // TODO: SCHEDULED POOL FOR GRASS
    private boolean isReproducible(Map.Entry<OrganismType, Set<Organism>> organisms) {
        return organisms.getValue().stream()
                .anyMatch(organism -> organism instanceof Reproducible);
    }
}

