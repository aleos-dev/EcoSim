package com.empty.ecosim.utils;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {
    private static final Random random = ThreadLocalRandom.current();

    public static <T extends OrganismType> T getRandomOrganismType(List<T> types) {
        // TODO WHAT THE HELL IS HAPPEN HERE
        if (types.size() == 0) return null;
        int index = random.nextInt(types.size());
        return types.get(index);
    }

    public static boolean didHuntSuccesses(double chance) {
        return random.nextDouble(100) < chance;
    }

    public static int getInt(int bound) {

        return random.nextInt(bound);
    }

    public static int getIntRange(int from, int to) {
        return random.nextInt(from, to);
    }

    public static Animal.Gender generateGender() {
        return random.nextInt(2) == 0 ? Animal.Gender.MALE : Animal.Gender.FEMALE;
    }

    public static Territory.Direction getRandomDirection(Cell startCell) {
        Territory.Direction[] directions = startCell.getAllowedDirections();

        return directions[random.nextInt(directions.length)];
    }
}
