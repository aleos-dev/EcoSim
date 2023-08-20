package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;

public abstract class Animal extends Organism implements Movable, Eater {
    public enum Gender {MALE, FEMALE}

    private static final double HUNGER_THRESHOLD = 0.8;

    private static final double DEPLETE_SATIETY_MODIFICATION = 0.1;
    private int speed;
    private double satiety;
    private Gender gender;
    private AnimalSpecification baseSpecification;
    private List<OrganismType> edibleTypes;


    @Override
    public abstract AnimalType getType();
    public abstract void eat(Organism food);
    public abstract Set<? extends Animal> reproduce();

    public void move() {
        depleteSatiety();
    }

    public void depleteSatiety() {

        satiety -= baseSpecification.maxSatiety() * DEPLETE_SATIETY_MODIFICATION;
        if (satiety <= 0.000001) {
            die();
            StatisticsCollector.registerStarvingCount(this.getType());
        }
    }

    @Override
    public boolean isEdible(OrganismType type) {
        return getEdibleTypes().contains(type);
    }
    public boolean canCaptureFood(OrganismType targetType) {
        return RandomGenerator.didHuntSuccesses(getBaseSpecification().getChanceToHunt(targetType));
    }

    public AnimalSpecification getBaseSpecification() {
        return baseSpecification;
    }

    protected <T extends Animal> T transferGeneticTraitsTo(T child) {
        child.setWeight(baseSpecification.weight());
        child.setSpeed(baseSpecification.speed());
        child.setSatiety(baseSpecification.maxSatiety());
        child.setBaseSpecification(baseSpecification);
        child.setGender(RandomGenerator.generateGender());
        child.setEdibleTypes(baseSpecification.edibleTypes());

        return child;
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

    @Override
    public List<OrganismType> getEdibleTypes() {
        return edibleTypes;
    }

    public void setEdibleTypes(Map<OrganismType, Double> edibleTypes) {
        this.edibleTypes = new ArrayList<>(edibleTypes.keySet());
    }
}
