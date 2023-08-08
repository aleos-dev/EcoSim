package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.predators.PredatorAnimal;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

import java.util.List;

public abstract class HerbivoreAnimal extends Animal {
    @Override
    public boolean tryToFindFoodAround(Cell cell) {
        sufferFromHunger();

        if (satiety > baseSpecification.maxSatiety() * 0.8) {
            return false;
        }

        List<Organism> grassList = cell.getResidentsByType(PlantType.GRASS);
        if (grassList == null || grassList.isEmpty()) {
            return false;
        }

        double nutrition = grassList.get(0).getWeight();
        int maxIntake = (int) ((baseSpecification.maxSatiety() - satiety) / nutrition) + 1;

        int numberOfFood = cell.getAndRemoveNumberOfResidentsByType(maxIntake, PlantType.GRASS);
        // Update the satiety based on the consumed food
        satiety = Math.min((satiety + nutrition * numberOfFood), baseSpecification.maxSatiety());

        return true;
    }

    public static class Caterpillar extends PredatorAnimal {


        @Override
        public AnimalType getType() {
            return AnimalType.FOX;
        }
    }
}
