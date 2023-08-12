package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.utils.RandomGenerator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grass extends Plant {
    private static final int maxSeed = 50;

    @Override
    public Set<? extends Plant> reproduce() {

        return Stream.generate(() -> {
                    Grass child = new Grass();
                    child.setWeight(weight);
                    return child;
                })
                .limit(RandomGenerator.getRandomInt(maxSeed))
                .collect(Collectors.toSet());
    }

    @Override
    public PlantType getType() {
        return PlantType.GRASS;
    }

}
