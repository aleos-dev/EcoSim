package com.empty.ecosim.utils;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class providing various randomization methods used throughout the application.
 */
public class RandomGenerator {

    /** Thread-safe random instance. */
    private static final Random random = ThreadLocalRandom.current();

    /**
     * Randomly select an organism type from the provided list.
     *
     * @param types List of organism types.
     * @param <T> Type of organism.
     * @return A randomly selected organism type or null if list is empty.
     */
    public static <T extends OrganismType> T getRandomOrganismType(List<T> types) {
        // TODO WHAT THE HELL IS HAPPEN HERE
        if (types.size() == 0) return null;
        int index = random.nextInt(types.size());
        return types.get(index);
    }

    /**
     * Determines if a hunting attempt was successful based on the provided chance.
     *
     * @param chance Probability of successful hunting attempt (in percentage).
     * @return True if hunt was successful, false otherwise.
     */
    public static boolean didHuntSuccesses(double chance) {
        return random.nextDouble(100) < chance;
    }


    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive)
     * and the specified bound (exclusive).
     *
     * @param bound Upper bound for the generated random number.
     * @return Generated random number.
     */
    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between the given
     * range (from inclusive, to exclusive).
     *
     * @param from Lower bound for the generated random number.
     * @param to Upper bound for the generated random number.
     * @return Generated random number.
     */
    public static int nextIntRange(int from, int to) {
        return random.nextInt(from, to);
    }

    /**
     * Randomly generates and returns an animal gender.
     *
     * @return Either MALE or FEMALE.
     */
    public static Animal.Gender generateGender() {
        return random.nextInt(2) == 0 ? Animal.Gender.MALE : Animal.Gender.FEMALE;
    }

    /**
     * Returns a random direction from the allowed directions of the given start cell.
     *
     * @param startCell Starting cell from which allowed directions are considered.
     * @return Random direction.
     */
    public static Territory.Direction getRandomDirection(Cell startCell) {
        Territory.Direction[] directions = startCell.getAllowedDirections();

        return directions[random.nextInt(directions.length)];
    }
}
