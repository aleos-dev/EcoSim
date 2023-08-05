package com.empty.ecosim.model;


import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Entity {

    protected AtomicBoolean isAlive;
    protected double weight;

    protected Entity() {
        isAlive = new AtomicBoolean(true);
    }

    public abstract EntityType getType();

    public boolean isAlive() {
        return isAlive.get();
    }


}
