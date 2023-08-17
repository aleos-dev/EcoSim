package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.controller.CycleController;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.OrganismSuperFactory;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Stream;

public class EcosystemSimulator {

    private StatisticsCollector statisticsCollector;
    private Territory territory;
    private CycleController controller;

    public EcosystemSimulator() {
        init();
    }

    public void runCycle() {
        statisticsCollector.calculateTerritoryStatistics(territory);
        controller.runCycle();
    }

    public StatisticsCollector getStatisticCollector() {
        return statisticsCollector;
    }

    public void clearStatistic() {
        statisticsCollector = new StatisticsCollector();
    }

    private void init() {
        statisticsCollector = new StatisticsCollector();
        territory = new Island();
        controller = new CycleController(territory);
        populateTerritory();

    }

    public void populateTerritory() {
        OrganismSuperFactory factory = new OrganismSuperFactory();

        List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
        List<PlantType> plantTypes = Arrays.asList(PlantType.values());

        territory.getCells().parallelStream().forEach(cell -> {
            Collections.shuffle(animalTypes);
            Collections.shuffle(plantTypes);

            generateOrganisms(cell, animalTypes, factory);
            generateOrganisms(cell, plantTypes, factory);
        });
    }

    private void generateOrganisms(Cell currentLocation, List<? extends OrganismType> typesList, OrganismSuperFactory factory) {
        typesList.stream()
                .limit(RandomGenerator.getRandomInt(typesList.size() + 1))
                .forEach(type -> Stream.generate(() -> factory.create(type))
                        .limit(RandomGenerator.getRandomInt(territory.getMaxResidentCountForOrganismType(type)))
                        .forEach(currentLocation::addResident));
    }
}
