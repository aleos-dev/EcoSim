package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class HerbivoreAnimal extends Animal {
    private static final double HUNGER_THRESHOLD = 0.80;
    private static final int MAX_OFFSPRING = 5;
    private static final int FERTILE_PERIOD = 10;

    @Override
    public void eat(Cell cell) {
        if (!isHungry()) return;

        var edibleTypesPresent = getEdibleTypesInCell(cell);
        if (edibleTypesPresent.isEmpty()) return;

        var targetType = RandomGenerator.getRandomOrganismType(edibleTypesPresent);

        if (cell.getResidentCountByType(targetType) > 0) {
            determineFoodType(cell, targetType);
        }
    }

    private void determineFoodType(Cell cell, OrganismType targetType) {
        if (targetType instanceof PlantType plantType) {
            consumePlant(cell, plantType);
        } else if (!isHuntFailed(targetType)) {
            consumeAnimal(cell, targetType);
        }
    }

    private void consumePlant(Cell cell, PlantType plantType) {
        List<Organism> targetList = cell.getResidentsCopyByType(plantType);
        double nutritionalValue = targetList.stream().findFirst().map(Organism::getWeight).orElse(0.0);
        double maxSatiety = getBaseSpecification().maxSatiety();
        int numberToIntakeToGetFull = (int) ((maxSatiety - getSatiety()) / nutritionalValue) + 1;
        int managedToEatNumber = cell.handleConsumptionProcess(numberToIntakeToGetFull, plantType);
        setSatiety(Math.min(getSatiety() + nutritionalValue * managedToEatNumber, maxSatiety));

        StatisticsCollector.registerPredationCount(plantType, managedToEatNumber);
    }

    private void consumeAnimal(Cell cell, OrganismType animalType) {
        Organism prey = cell.getOrganismForConsumption(animalType);
        if (prey == null) return;

        setSatiety(Math.min(getSatiety() + prey.getWeight(), getBaseSpecification().maxSatiety()));
    }

    private boolean isHungry() {
        return satiety <= getBaseSpecification().maxSatiety() * HUNGER_THRESHOLD;
    }

    private List<OrganismType> getEdibleTypesInCell(Cell cell) {
        var edibleTypesPresent = new ArrayList<>(cell.getPresentTypes());
        edibleTypesPresent.retainAll(edibleTypes);
        return edibleTypesPresent;
    }

    @Override
    public int maxOffspring() {
        return MAX_OFFSPRING;
    }

    public int getFertilePeriod() {
        return FERTILE_PERIOD;
    }


    private boolean isHuntFailed(OrganismType targetType) {
        return RandomGenerator.isHuntFailed(getBaseSpecification().getChanceToHunt(targetType));
    }
}
