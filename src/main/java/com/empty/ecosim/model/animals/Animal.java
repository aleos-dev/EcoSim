package com.empty.ecosim.model.animals;

public abstract class Animal {
    protected double weight;
    protected double speed;
    protected double satiety;

    public abstract void eat();
    public abstract void reproduce();
    public abstract void move();

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "weight=" + weight +
                ", speed=" + speed +
                ", satiety=" + satiety +
                '}';
    }
}
