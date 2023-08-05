package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.island.Cell;

import java.util.List;
import java.util.Map;


public abstract class Animal extends Entity {

    protected double speed;
    protected double satiety;
    protected Map<EntityType, Double> edibleTypes;


    public abstract void reproduce();
    public abstract void move();

    protected Entity findFood(Cell cell) {
//        edibleTypes.forEach();
        cell.getEntitiesFor(this.getType());
        return null;
    }

    public abstract void eat(Entity entity);
    @Override
    public abstract AnimalType getType();

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


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

    public Map<EntityType, Double> getEdibleTypes() {
        return edibleTypes;
    }

    public void setEdibleTypes(Map<EntityType, Double> edibleTypes) {
        this.edibleTypes = edibleTypes;
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
