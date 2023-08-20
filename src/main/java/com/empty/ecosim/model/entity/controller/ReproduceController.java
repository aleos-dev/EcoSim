package com.empty.ecosim.model.entity.controller;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.empty.ecosim.model.entity.organism.plants.PlantType.GRASS;


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
            territory.getCells().parallelStream().forEach(this::runPlantsGrowthAt);
        } catch (Throwable e) {
            System.out.println("RUN PLANTS GROWTH EXCEPTION");
            e.printStackTrace();
            System.exit(21);
        }
    }

    private void runReproduceForAnimalsAt(Cell cell) {
        cell.lock();
        cell.getResidentsMap().entrySet().stream()
                .filter(this::isAnimalsCollection)
                .forEach(this::generateNewborns);
        cell.unlock();
    }


    private final PlantFactory factory = new SimplePlantFactory();

    private void runPlantsGrowthAt(Cell cell) {
        cell.lock();
        AtomicInteger availableCellSpace = new AtomicInteger();
        try {
            Arrays.stream(PlantType.values()).forEach(type -> {
                availableCellSpace.set(getMaxAvailableCapacityFor(type, cell.getOrganismsByType(type)));
                int growthNumber = (int) (RandomGenerator.getIntRange(0, availableCellSpace.get()) * Plant.getPlantsGrowthMultiplier());
                List<Plant> plants = Stream.generate(() -> factory.create(type))
                        .limit(growthNumber)
                        .toList();

                cell.getResidentsMap().get(type).addAll(plants);

            });
        } catch (IllegalArgumentException e) {
            System.out.println(availableCellSpace.get());
        }
        cell.unlock();

    }

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

    private boolean isAnimalsCollection(Map.Entry<OrganismType, Set<Organism>> entry) {
        return entry.getKey() instanceof AnimalType;
    }

    private int getMaxAvailableCapacityFor(OrganismType organismType, Set<Organism> residents) {
        return Math.max(territory.getMaximumCapacityFor(organismType) - residents.size(), 0);

    }
}

