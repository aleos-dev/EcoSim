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

        Organism prey = getPreyToHunt(cell);
        if (prey == null) {
            return false;
        }

        consume(prey);

        return true;
    }

    protected Organism getPreyToHunt(Cell cell) {
        var preyType = getRandomPreyType();

//        preyType = AnimalType.SHEEP;
        if (RandomGenerator.isHuntFailed(baseSpecification.getChanceToHunt(preyType))) {
            return null;
        }
        return cell.getAnyAndRemove(preyType);
    }

    protected void consume(Organism food) {
        if (food == null) return;
        satiety = Math.min(satiety + food.getWeight(), baseSpecification.maxSatiety());
    }

   private OrganismType getRandomPreyType() {
        return RandomGenerator.getRandomType(edibleTypes);
   }

}
