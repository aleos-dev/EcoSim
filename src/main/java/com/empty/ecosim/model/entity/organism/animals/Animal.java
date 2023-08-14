package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;

public abstract class Animal extends Organism implements Movable, Eater {
    public enum Gender {MALE, FEMALE}

    private int speed;
    protected double satiety;
    protected Gender gender;
    private AnimalSpecification baseSpecification;
    protected List<OrganismType> edibleTypes;

    public void move(Territory territory, Cell currentCell) {
        sufferFromHunger();
        if(!isAlive()) {
            currentCell.removeResidentFromCell(this);
            StatisticsCollector.registerStarvingCount(this.getType());
            return;
        }

        Cell destinationCell = territory.getRandomAdjacentCell(currentCell, speed);
        if (destinationCell == null || destinationCell == currentCell) {
            return;
        }

        territory.beginTravel(this, currentCell, destinationCell);
    }

    public abstract void eat(Cell cell);

    public abstract Set<? extends Animal> reproduce();

    public abstract int maxOffspring();
    @Override
    public abstract AnimalType getType();

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
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
        if (satiety <= 0) {
            isAlive.set(false);
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    protected <T extends Animal> Animal copyGenesTo(T child) {
        child.setWeight(baseSpecification.weight());
        child.setSpeed(baseSpecification.speed());
        child.setSatiety(baseSpecification.maxSatiety());
        child.setBaseSpecification(baseSpecification);
        child.setGender(RandomGenerator.generateGender());
        child.setEdibleTypes(baseSpecification.edibleTypes());

        return child;
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
