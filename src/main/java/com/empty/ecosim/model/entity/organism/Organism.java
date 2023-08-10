package com.empty.ecosim.model.entity.organism;


import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Organism implements Reproducible {

    protected AtomicBoolean isAlive;
    protected double weight;

    protected Organism() {
        isAlive = new AtomicBoolean(true);
    }

    public abstract OrganismType getType();

    public boolean isAlive() {
        return isAlive.get();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }



}
