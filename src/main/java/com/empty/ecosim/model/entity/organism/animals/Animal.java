package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.statistics.StatisticsCollector;

import java.util.*;


public abstract class Animal extends Organism implements Movable, Eater {

    public enum Gender {MALE, FEMALE}

    protected double speed;
    protected double satiety;
    protected Gender gender;
    protected AnimalSpecification baseSpecification;
    protected List<OrganismType> edibleTypes;

    public void move(Territory territory, Cell currentCell) {
        sufferFromHunger();
        if(!isAlive()) {
            currentCell.removeResidentFromCell(this);
            int amountOfDeadOrganism = 1;
            StatisticsCollector.registerStarvingProcess(amountOfDeadOrganism);
            return;
        }

        Cell destinationCell = territory.getRandomPossibleDestination(currentCell, (int) speed);
        if (destinationCell == null || destinationCell == currentCell) {
            return;
        }

        territory.beginTravel(this, currentCell, destinationCell);
    }

    public abstract boolean findFoodAt(Cell cell);

    public abstract Set<? extends Animal> reproduce();

    public abstract int maxOffspring();
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

    public abstract int getFertilePeriod();

    protected void sufferFromHunger() {

        satiety -= baseSpecification.maxSatiety() / 10;
        if (satiety < 0) {
            isAlive.set(false);
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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
