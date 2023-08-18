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
import java.util.stream.Collectors;

public abstract class Animal extends Organism implements Movable, Eater {
    public enum Gender {MALE, FEMALE}

    private static final double HUNGER_THRESHOLD = 0.8;


    private int speed;
    private double satiety;
    private Gender gender;
    private AnimalSpecification baseSpecification;
    private List<OrganismType> edibleTypes;

    @Override
    public abstract AnimalType getType();
    public abstract void eat(Cell cell);
    public abstract Set<? extends Animal> reproduce();

    public void move() {
        spendEnergy();
    }
    

    public void spendEnergy() {

        satiety -= baseSpecification.maxSatiety() / 10;
        if (satiety <= 0.000001) {
            markAsDead();
            StatisticsCollector.registerStarvingCount(this.getType());
            StatisticsCollector.decreasePopulationCount(this.getType(), 1);
        }
    }
    
    protected boolean isHungry() {
        return getSatiety() < getBaseSpecification().maxSatiety() * HUNGER_THRESHOLD;
    }

    protected boolean isEdible(OrganismType type) {
        return getEdibleTypes().contains(type);
    }
    protected List<OrganismType> filterEdibleTypesInCell(Cell cell) {
        return cell.getPresentOrganismTypes().stream()
                .filter(this::isEdible)
                .collect(Collectors.toList());
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

    public AnimalSpecification getBaseSpecification() {
        return baseSpecification;
    }

    public void setBaseSpecification(AnimalSpecification baseSpecification) {
        this.baseSpecification = baseSpecification;
    }
    public abstract int getFertilePeriod();
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
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
}
