package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

import java.util.List;

public abstract class HerbivoreAnimal extends Animal {
    @Override
    public boolean tryToFindFoodAround(Cell cell) {
        List<Organism> grassList = cell.getResidentListIfPresent(PlantType.GRASS);
        if (grassList == null || grassList.isEmpty()) {
            return false;
        }

        double nutrition = grassList.get(0).getWeight();
        int quantityToConsume = (int) ((baseSpecification.maxSatiety() - satiety) / nutrition);
        int quantityOfFood = Math.min(grassList.size(), quantityToConsume);

        grassList.subList(0, quantityOfFood).clear();

        // Update the satiety based on the consumed food
        satiety = Math.min((satiety + nutrition * quantityOfFood), baseSpecification.maxSatiety());

        return true;
    }
}
