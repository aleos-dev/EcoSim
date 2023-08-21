package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.model.entity.organism.Organism;

import java.util.Set;

public abstract class Plant extends Organism {


    public abstract Set<? extends Plant> reproduce();
    @Override
    public abstract PlantType getType();

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
