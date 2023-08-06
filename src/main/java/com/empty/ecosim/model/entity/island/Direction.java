package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.utils.RandomGenerator;

public enum Direction {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    final int x;
    final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getRandomDirection() {
        return RandomGenerator.getDirection();
    }

}
