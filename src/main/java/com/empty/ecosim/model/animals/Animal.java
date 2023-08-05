package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.island.Cell;
import com.empty.ecosim.model.island.Direction;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;


public abstract class Animal extends Entity {

    protected double speed;
    protected double satiety;
    protected AnimalSpecification baseSpecification;
    protected List<EntityType> edibleTypes;


    public abstract void reproduce();

    public Direction move() {
        return RandomGenerator.getDirection();
    }

    public void findFoodAndEat(Cell cell) {
        double maxSatiety = baseSpecification.maxSatiety();
        if (satiety > maxSatiety * 0.8) {
            return;
        }

        var preyType = RandomGenerator.getRandomType(edibleTypes);
//        preyType = AnimalType.SHEEP;
        Set<Entity> preys = cell.getEntitiesFor(preyType);
        if (preys == null || RandomGenerator.isHuntFailed(baseSpecification.getChanceToHunt(preyType))) {
            return;
        }

        Iterator<Entity> preyIterator = preys.iterator();
        while (preyIterator.hasNext() && satiety < maxSatiety) {
            Entity prey = preyIterator.next();
//            System.out.println("Iterator - prey is: " + prey);
//            System.out.println("Satiety before is: " + satiety);
            satiety += prey.getWeight();
//            System.out.println("Satiety after is: " + satiety);
            cell.remove(prey);
        }

        satiety = Math.min(satiety, maxSatiety);
//        System.out.println("Satiety end is: " + satiety);
    }

    public abstract void eat(Entity entity);

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

    public List<EntityType> getEdibleTypes() {
        return edibleTypes;
    }

    public void setEdibleTypes(Map<EntityType, Double> edibleTypes) {
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
