package com.empty.ecosim.model.plants;

import com.empty.ecosim.model.Entity;

public abstract class Plant extends Entity {

    public abstract void reproduce();

    @Override
    public abstract PlantType getType();

    public double getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "isAlive=" + isAlive +
                ", weight=" + weight +
                '}';
    }
}
