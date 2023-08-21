package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.OrganismType;
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

    @Override
    public TerritoryType getType() {
        return ISLAND;
    }

    public void updateCellCapacityFor(OrganismType type, double multiplier) {
        islandSpecification.organismCapacity().compute(type, (k, v) -> {
            return v == null ? 0 : (int) (v * multiplier);
        });
    }
    public int getMaxResidentCountForOrganismType(OrganismType type) {
        return islandSpecification.organismCapacity().get(type);
    }

    @Override
    public Cell getRandomDestination(Cell cell, int speed) {
        Cell destination = getNextDestination(cell);
        return speed < 2 ? destination : getRandomDestination(destination, speed - 1);
    }

    private void initializeCellGrids() {
        grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(this, j, i, calculateAvailableDirections(j, i));
            }
        }
    }


    private Direction[] calculateAvailableDirections(int x, int y) {
        Set<Direction> directions = EnumSet.noneOf(Direction.class);

        if (isWestAvailable(x)) directions.add(Direction.WEST);
        if (isEastAvailable(x)) directions.add(Direction.EAST);
        if (isNorthAvailable(y)) directions.add(Direction.NORTH);
        if (isSouthAvailable(y)) directions.add(Direction.SOUTH);

        return directions.toArray(new Direction[0]);
    }

    private boolean isWestAvailable(int x) {
        return x > 0;
    }

    private boolean isEastAvailable(int x) {
        return x < width - 1;
    }

    private boolean isNorthAvailable(int y) {
        return y > 0;
    }

    private boolean isSouthAvailable(int y) {
        return y < height - 1;
    }

    private Cell getNextDestination(Cell start) {
        Direction nextDirection = RandomGenerator.getRandomDirection(start);
        int x = start.getX() + nextDirection.getX();
        int y = start.getY() + nextDirection.getY();
        return grid[y][x];
    }
}
