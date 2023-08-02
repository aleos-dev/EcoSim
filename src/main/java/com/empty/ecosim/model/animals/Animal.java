package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.Edible;

import java.util.Map;

public abstract class Animal {
    protected int weight;
    protected int speed;
    protected Map<Animal.Type, String> edibleTypes;
    public enum Type implements Edible {
        WOLF, HORSE
    }

    public abstract void eat();
    public abstract void reproduce();
    public abstract void move();

}
