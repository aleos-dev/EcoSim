package com.empty.ecosim.model.entity.organism;


public abstract class Organism implements Reproducible {

    protected boolean isAlive;
    protected double weight;

    protected Organism() {
        isAlive = true;
    }

    public abstract OrganismType getType();

    public boolean isDead() {
        return !isAlive;
    }
    public void die() {
        isAlive = false;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }



}
