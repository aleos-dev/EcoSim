package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.animals.AnimalBaseSpecification.AnimalSpec;

public abstract class Animal {
    enum Type {
        WOLF
    }

    public static void main(String[] args) {
        AnimalSpec as = AnimalBaseSpecification.getSpec(Type.WOLF);
    }
    public abstract void eat();
    public abstract void reproduce();
    public abstract void move();

}
