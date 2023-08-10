package com.empty.ecosim.utils;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;

import java.util.List;
import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();
    public static <T extends OrganismType> T getRandomType(List<T> entityTypes) {
        return entityTypes.get(random.nextInt(entityTypes.size()));
    }

    public static boolean isHuntFailed(double chance) {
        return random.nextDouble(100) >= chance;
    }

    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public static Animal.Gender generateGender() {
        return random.nextInt() % 2 == 0 ? Animal.Gender.MALE : Animal.Gender.FEMALE;
    }
}
