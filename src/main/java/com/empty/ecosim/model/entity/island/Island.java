package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.herbivores.Caterpillar;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.empty.ecosim.model.entity.island.TerritoryType.ISLAND;

public class Island extends Territory {

    private final int width;
    private final int height;
    private Cell[][] grid;
    private final IslandSpecification islandSpecification = TERRITORY_SPECIFICATION.getSpecificationForType(TerritoryType.ISLAND);

    public Island() {
        this.width = islandSpecification.width();
        this.height = islandSpecification.height();

        initializeCellGrids();
        super.cells = Arrays.stream(grid).flatMap(Arrays::stream).collect(Collectors.toList());

    }


    public void updateCellCapacityFor(OrganismType type, double multiplier) {
        islandSpecification.organismCapacity().compute(type, (k, v) -> (int) (v * multiplier));
    }

    private void initializeCellGrids() {
        grid = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(j, i, calculateExistedRoutes(j, i));

            }
        }
    }

    @Override
    public TerritoryType getType() {
        return ISLAND;
    }

    @Override
    public Cell getRandomDestination(Cell cell, int speed) {
        Cell destination = getNextDestination(cell);

        if (speed < 2) return destination;

        return getRandomDestination(destination, speed - 1);
    }

    public int getMaxResidentCountForOrganismType(OrganismType type) {
        return islandSpecification.organismCapacity().get(type);
    }

    private Direction[] calculateExistedRoutes(int x, int y) {
        Set<Direction> directions = EnumSet.noneOf(Direction.class);

        checkWestDirection(x, directions);
        checkEastDirection(x, directions);
        checkNorthDirection(y, directions);
        checkSouthDirection(y, directions);

        return directions.toArray(new Direction[0]);
    }

    private boolean isCoordinateValid(int x, int y) {
        return isXCoordinateValid(x) && isYCoordinateValid(y);
    }

    private void checkWestDirection(int x, Set<Direction> directions) {
        if (x != 0) {
            directions.add(Direction.WEST);
        }
    }

    private void checkEastDirection(int x, Set<Direction> directions) {
        if (x != width -1) {
            directions.add(Direction.EAST);
        }
    }

    private void checkNorthDirection(int y, Set<Direction> directions) {
        if (y != 0) {
            directions.add(Direction.NORTH);
        }
    }

    private void checkSouthDirection(int y, Set<Direction> directions) {
        if (y != height -1) {
            directions.add(Direction.SOUTH);
        }
    }

    private boolean isXCoordinateValid(int x) {
        return x >= 0 && x < width;
    }

    private boolean isYCoordinateValid(int y) {
        return y >= 0 && y < height;
    }

    private Cell getNextDestination(Cell start) {
        Direction nextDirection = RandomGenerator.getRandomDirection(start);
        int x = start.getX() + nextDirection.getX();
        int y = start.getY() + nextDirection.getY();

        return grid[y][x];
    }

}
