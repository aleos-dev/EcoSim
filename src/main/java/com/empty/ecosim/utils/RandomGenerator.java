package com.empty.ecosim.utils;

import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.island.Direction;

import java.util.List;
import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();
    public static <T extends EntityType> T getRandomType(List<T> entityTypes) {
        return entityTypes.get(random.nextInt(entityTypes.size()));
    }

    public static boolean isHuntFailed(double chance) {
        return random.nextDouble(100) >= chance;
    }

    public static Direction getDirection() {
        return Direction.values()[random.nextInt(4)];
    }
}
