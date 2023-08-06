package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.model.entity.organism.Organism;

public abstract class Plant extends Organism {

    public Plant reproduce(){
        return null;
    }
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
