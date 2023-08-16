package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.controller.FeedingController;
import com.empty.ecosim.model.entity.controller.MovementController;
import com.empty.ecosim.model.entity.controller.ReproduceController;
import com.empty.ecosim.model.entity.island.Island;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;

public class Main {


    public static void main(String[] args) throws IllegalArgumentException {

        StatisticsCollector statColl = new StatisticsCollector();
        Territory island = new Island();
        MovementController mc = new MovementController(island);
        FeedingController fc = new FeedingController(island);
        ReproduceController rc = new ReproduceController(island);

        statColl.calculateTerritoryStatistics(island);
        initIsland(island);

        for (int i = 0; i < 300; i++) {
             if(i == 290) {
                 System.out.println();
             }
            statColl.calculateTerritoryStatistics(island);

            fc.executeFeedingCycle();
            mc.executeMovementCycle();
            rc.initiateReproduction();

            System.out.println(statColl);
        }
    }

    public static void initIsland(Territory island) {
        AnimalFactory animalFactory = new SimpleAnimalFactory();
        PlantFactory plantFactory = new SimplePlantFactory();

        List<AnimalType> animalTypes = Arrays.asList(AnimalType.values());
        List<PlantType> plantTypes = Arrays.asList(PlantType.values());

        island.getCells().forEach(cell -> {

            Collections.shuffle(animalTypes);
            int varietyAnimalTypes = RandomGenerator.getRandomInt(animalTypes.size() + 1);
            animalTypes.stream().limit(varietyAnimalTypes).forEach(animalType -> {
                int animalsNumber = RandomGenerator.getRandomInt(island.getMaxResidentCountForOrganismType(animalType));
                for (int i = 0; i < animalsNumber; i++) {
                    cell.addResident(animalFactory.create(animalType));
                }
            });

            Collections.shuffle(plantTypes);
            int varietyPlantTypes = RandomGenerator.getRandomInt(plantTypes.size() + 1);
            plantTypes.stream().limit(varietyPlantTypes).forEach(plantType -> {
                int plantsNumber = RandomGenerator.getRandomInt(island.getMaxResidentCountForOrganismType(plantType));
                for (int i = 0; i < plantsNumber; i++) {
                    cell.addResident(plantFactory.create(plantType));
                }
            });
        });
    }

}
