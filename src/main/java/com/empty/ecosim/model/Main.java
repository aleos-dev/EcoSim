package com.empty.ecosim.model;

import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.EntityBaseSpecification;
import com.empty.ecosim.model.plants.Plant;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {
        EntityBaseSpecification.AnimalSpec as = EntityBaseSpecification.getSpec(Animal.Type.WOLF);
        System.out.println(as);
        as = EntityBaseSpecification.getSpec(Animal.Type.HORSE);
        System.out.println(as);

        EntityBaseSpecification.PlantSpec ps = EntityBaseSpecification.getSpec(Plant.Type.GRASS);
        System.out.println(ps);
    }
}
