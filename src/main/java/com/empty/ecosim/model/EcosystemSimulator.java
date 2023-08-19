package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.controller.FeedingController;
import com.empty.ecosim.model.entity.controller.MovementController;
import com.empty.ecosim.model.entity.controller.ReproduceController;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.OrganismSuperFactory;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class EcosystemSimulator implements Runnable {

    private StatisticsCollector statisticsCollector;
    private Territory territory;
    private ScheduledExecutorService executor;
    private FeedingController fc;
    private MovementController mc;
    private ReproduceController rc;

    public EcosystemSimulator(ScheduledExecutorService executor) {
        this.executor = executor;
        init();
    }

    public void start() {
        executor.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(statisticsCollector, 1, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(() -> rc.runPlantsGrowth(), 0, 1000, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(() -> System.out.println(System.currentTimeMillis()), 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            fc.executeFeeding();
            mc.executeMovement();
            rc.executeReproductionForAnimals();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void init() {
        statisticsCollector = new StatisticsCollector();
        territory = new Island();
        fc = new FeedingController(territory);
        mc = new MovementController(territory);
        rc = new ReproduceController(territory);
        populateTerritory();

    }

    public void populateTerritory() {
        OrganismSuperFactory factory = new OrganismSuperFactory();

        List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
        List<PlantType> plantTypes = Arrays.asList(PlantType.values());

        territory.getCells().parallelStream().forEach(cell -> {
            Collections.shuffle(animalTypes);
            Collections.shuffle(plantTypes);

            generateOrganisms(cell, new ArrayList<>(animalTypes), factory);
            generateOrganisms(cell, new ArrayList<>(plantTypes), factory);
        });
    }

    private void generateOrganisms(Cell currentLocation, List<? extends OrganismType> typesList, OrganismSuperFactory factory) {
        typesList.stream()
                .limit(RandomGenerator.getIntRange(1, typesList.size() + 1))
                .forEach(type -> Stream.generate(() -> factory.create(type))
                        .limit(RandomGenerator.getIntRange(1, territory.getMaxResidentCountForOrganismType(type)))
                        .forEach(currentLocation::addResident));
    }

}
