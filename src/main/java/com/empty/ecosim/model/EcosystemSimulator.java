package com.empty.ecosim.model;

import com.empty.ecosim.model.configuration.userSetup.UserSetupManager;
import com.empty.ecosim.model.entity.controller.FeedingController;
import com.empty.ecosim.model.entity.controller.MovementController;
import com.empty.ecosim.model.entity.controller.ReproduceController;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.OrganismSuperFactory;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Simulates an ecosystem with various organisms and their interactions.
 * <p>
 * This simulator uses different controllers to simulate feeding, movement, and reproduction behaviors.
 * It also utilizes a scheduled executor service to repeatedly perform the simulation and update statistics.
 * </p>
 */
public class EcosystemSimulator implements Runnable {

    private final ScheduledExecutorService executor;
    private StatisticsCollector statisticsCollector;
    private Territory territory;
    private FeedingController fc;
    private MovementController mc;
    private ReproduceController rc;
    ScheduledFuture<?> mainExecutor;
    ScheduledFuture<?> statisticCollectorExecutor;
    ScheduledFuture<?> growPlantExecutor;

    /**
     * Constructs a new EcosystemSimulator.
     *
     * @param executor The executor service to be used for scheduling tasks.
     */
    public EcosystemSimulator(ScheduledExecutorService executor) {
        this.executor = executor;
        init();
    }

    /**
     * Starts the ecosystem simulation.
     * <p>
     * This method reloads user configurations, sets up organisms based on user input,
     * and initiates various scheduled tasks for the main simulation execution,
     * statistics collection, and plant growth.
     * </p>
     */
    public void start() {
        reloadUserConfiguration();
        mainExecutor = executor.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);
        statisticCollectorExecutor = executor.scheduleAtFixedRate(statisticsCollector, 50, (long) Optional.of(UserSetupManager.INSTANCE.get().printStatisticInterval()).orElse(1000), TimeUnit.MILLISECONDS);
        growPlantExecutor = executor.scheduleAtFixedRate(() -> rc.runPlantsGrowth(), 0, 500, TimeUnit.MILLISECONDS);

    }

    /**
     * Stops the ecosystem simulation and prompts the user for further actions.
     */
    public void stop() {
        mainExecutor.cancel(false);
        statisticCollectorExecutor.cancel(false);
        growPlantExecutor.cancel(false);

        System.out.println("Now you can correct configuration, continue : \"y/n\" ");
    }

    /**
     * Executes one cycle of the ecosystem simulation, covering feeding,
     * movement, and reproduction behaviors of the organisms.
     */
    @Override
    public void run() {
        fc.executeFeeding();
        mc.executeMovement();
        rc.executeReproductionForAnimals();

    }

    /**
     * Initializes the ecosystem simulator with default or initial values,
     * creating controllers, setting up initial populations, and gathering first statistics.
     */
    private void init() {

        statisticsCollector = new StatisticsCollector();
        territory = new Island();
        fc = new FeedingController(territory);
        mc = new MovementController(territory);
        rc = new ReproduceController(territory);
        initTerritoryPopulation();
        statisticsCollector.calculateTerritoryStatistics();
        StatisticsCollector.resetNewbornCount();
    }

    /**
     * Sets up the initial population of the territory.
     * <p>
     * Organisms are generated based on the types available and added to the cells
     * within the territory.
     * </p>
     */
    private void initTerritoryPopulation() {
        OrganismSuperFactory factory = new OrganismSuperFactory();

        List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
        List<PlantType> plantTypes = Arrays.asList(PlantType.values());

        territory.getCells().parallelStream().forEach(cell -> {
            generateOrganisms(cell, new ArrayList<>(animalTypes), factory);
            generateOrganisms(cell, new ArrayList<>(plantTypes), factory);
        });
    }

    /**
     * Generates organisms of specific types and adds them to a cell.
     *
     * @param cell      The cell to which organisms are to be added.
     * @param typesList A list of organism types for which organisms are to be generated.
     * @param factory   The factory responsible for creating organisms of a specific type.
     */
    private void generateOrganisms(Cell cell, List<? extends OrganismType> typesList, OrganismSuperFactory factory) {
        typesList.forEach(type -> Stream.generate(() -> factory.create(type)).limit(organismCountOnCell(type)).forEach(cell::addOrganism));
    }

    /**
     * Determines the count of a specific type of organism that should reside on a cell.
     *
     * @param type The type of organism.
     * @return The count of organisms of the specified type that should be on a cell.
     */
    private int organismCountOnCell(OrganismType type) {
        int max = territory.getMaxResidentCountForOrganismType(type);
        if (max == 0) {
            return 0;
        }

        int count = UserSetupManager.INSTANCE.get().startOrganismTypeCountOnCell().getOrDefault(type, RandomGenerator.nextIntRange(1, territory.getMaxResidentCountForOrganismType(type)) / 2);

        return Math.min(count, max);
    }

    private void reloadUserConfiguration() {
        UserSetupManager.INSTANCE.reload();
        territory.loadUserSetupForMaxOrganismCountOnCell();
        SimpleAnimalFactory.applyUserSetup();
    }
}
