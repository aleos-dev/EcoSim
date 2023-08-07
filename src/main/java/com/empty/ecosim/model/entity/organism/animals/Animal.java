package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Movable;

import java.util.*;


public abstract class Animal extends Organism implements Movable, Eater {

    protected double speed;
    protected double satiety;
    protected AnimalSpecification baseSpecification;
    protected List<OrganismType> edibleTypes;

    public void move(Territory territory, Cell currentCell) {

        Cell destinationCell = territory.getPossibleDestinationBasedOnSpeed(currentCell, (int) speed);
        if (destinationCell == null || destinationCell == currentCell) {
            return;
        }
        territory.moveResidentFromTo(this, currentCell, destinationCell);
    }

    public abstract boolean tryToFindFoodAround(Cell cell);

    public Animal reproduce() {
        return null;
    }

    @Override
    public abstract AnimalType getType();

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSatiety() {
        return satiety;
    }

    public void setSatiety(double satiety) {
        this.satiety = satiety;
    }

    public List<OrganismType> getEdibleTypes() {
        return edibleTypes;
    }

    public void setEdibleTypes(Map<OrganismType, Double> edibleTypes) {
        this.edibleTypes = new ArrayList<>(edibleTypes.keySet());
    }

    public AnimalSpecification getBaseSpecification() {
        return baseSpecification;
    }

    public void setBaseSpecification(AnimalSpecification baseSpecification) {
        this.baseSpecification = baseSpecification;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "speed=" + speed +
                ", satiety=" + satiety +
                ", isAlive=" + isAlive +
                ", weight=" + weight +
                '}';
    }
}
