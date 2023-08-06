package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.List;

public abstract class PredatorAnimal extends Animal {
    public boolean tryToFindFoodAround(Cell cell) {
        if (satiety > baseSpecification.maxSatiety() * 0.8) {
            return false;
        }

        List<Organism> preyList = getPreyToHunt(cell);
        if (preyList == null || preyList.size() == 0) {
            return false;
        }

        Organism prey = preyList.get(0);
        consume(prey);

//        cell.remove() preys.remove dont delete residents list if it equals 0
        preyList.remove(0);

        return true;
    }

    protected List<Organism> getPreyToHunt(Cell cell) {
        var preyType = getRandomPreyType();

//        preyType = AnimalType.SHEEP;

        List<Organism> preys = cell.getResidentIfPresent(preyType);
        if (preys == null || RandomGenerator.isHuntFailed(baseSpecification.getChanceToHunt(preyType))) {
            return null;
        }
        return preys;
    }

    protected void consume(Organism food) {
        if (food == null) return;
        satiety = Math.min(satiety + food.getWeight(), baseSpecification.maxSatiety());
    }

   private OrganismType getRandomPreyType() {
        return RandomGenerator.getRandomType(edibleTypes);
   }

}
