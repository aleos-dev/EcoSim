package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.model.entity.organism.Organism;

import java.util.Set;

public abstract class Plant extends Organism {

    private static final double PLANTS_GROWTH_MULTIPLIER = 0.3;

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

    public static double getPlantsGrowthMultiplier() {
        return PLANTS_GROWTH_MULTIPLIER;
    }
}
