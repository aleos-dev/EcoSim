package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Consumer;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Direction;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;


public abstract class Animal extends Organism implements Movable, Consumer {

    protected double speed;
    protected double satiety;
    protected AnimalSpecification baseSpecification;
    protected List<OrganismType> edibleTypes;


    public Cell move(Cell coordinate) {
        cell.get
        Direction.getRandomDirection();
    }

    public void findFood(Cell cell) {
        double maxSatiety = baseSpecification.maxSatiety();
        if (satiety > maxSatiety * 0.8) {
            return;
        }

        var preyType = RandomGenerator.getRandomType(edibleTypes);
//        preyType = AnimalType.SHEEP;
        Set<Organism> preys = cell.getEntitiesFor(preyType);
        if (preys == null || RandomGenerator.isHuntFailed(baseSpecification.getChanceToHunt(preyType))) {
            return;
        }

        Iterator<Organism> preyIterator = preys.iterator();
        while (preyIterator.hasNext() && satiety < maxSatiety) {
            Organism prey = preyIterator.next();
//            System.out.println("Iterator - prey is: " + prey);
//            System.out.println("Satiety before is: " + satiety);
            satiety += prey.getWeight();
//            System.out.println("Satiety after is: " + satiety);
            cell.remove(prey);
        }

        satiety = Math.min(satiety, maxSatiety);
//        System.out.println("Satiety end is: " + satiety);
    }

    public void consume(Organism organism) {

    }

    public void eat(Organism organism) {
    }

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
