package com.empty.ecosim.statistics;

import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IslandStatistic implements Statistic {
    private Map<AnimalType, Integer> animals = new HashMap<>();
    private Map<PlantType, Integer> plants = new HashMap<>();
    private int numberOfAnimals;
    private int numberOfPlants;
    private int becameFood;
    private int dieOfHunger;
    public IslandStatistic calculateIslandStatistic(Territory island) {
        Arrays.stream(AnimalType.values()).forEach(type -> animals.put(type, 0));

        island.getCells()
                .forEach(cell -> {
                   Arrays.stream(AnimalType.values())
                           .forEach(animalType -> animals.compute(animalType, (type, count) -> count + cell.getResidentCountByType(type)));
                   becameFood += cell.getBecameFood();
                   cell.setBecameFood(0);
                   dieOfHunger += cell.getDieOfHunger();
                   cell.setDieOfHunger(0);
                });


        Arrays.stream(PlantType.values()).forEach(type -> plants.put(type, 0));
        island.getCells()
                .forEach(cell -> {
                    Arrays.stream(PlantType.values())
                            .forEach(plantType -> plants.compute(plantType, (type, count) -> count + cell.getResidentCountByType(type)));
                });

         return this;
    }

    public int getNumberOfAnimals() {
        numberOfAnimals = animals.values().stream().mapToInt(Integer::intValue).sum();
        return numberOfAnimals;
    }

    public long getNumberOfPlants() {
        numberOfPlants = plants.values().stream().mapToInt(Integer::intValue).sum();
        return numberOfPlants;
    }

    public int getBecameFood() {
        return becameFood;
    }

    public int getDieOfHunger() {
        return dieOfHunger;
    }

    @Override
    public String toString() {
        return "IslandStatistic{" +
                "animals=" + animals +
                ", plants=" + plants +
                '}';
    }
}
