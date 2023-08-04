package com.empty.ecosim.model.plants;

import com.empty.ecosim.model.EntityType;

public abstract class Plant implements EntityType {

    protected int weight;

    public abstract void reproduce();

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "weight=" + weight +
                '}';
    }
}
