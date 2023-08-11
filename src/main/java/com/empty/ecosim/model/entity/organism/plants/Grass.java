package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.utils.RandomGenerator;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grass extends Plant {
    private static final int maxSeed = 8;
    private static final int FERTILE_PERIOD = 10;

    @Override
    public Set<? extends Plant> reproduce() {
        if (RandomGenerator.getRandomInt(FERTILE_PERIOD) > 0) {
            return Collections.emptySet();
        }
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
