package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.userSetup.UserSetupManager;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;
import java.util.stream.Collectors;

import static com.empty.ecosim.model.entity.island.TerritoryType.ISLAND;

/**
 * Represents an Island which is a type of Territory. This class manages cells in a grid format that
 * contains organisms and provides utility methods to interact with the island's ecosystem.
 */
public class Island extends Territory {

    private final int width;
    private final int height;
    private Cell[][] grid;
    private final IslandSpecification islandSpecification = TERRITORY_SPECIFICATION.getSpecificationForType(TerritoryType.ISLAND);

    /**
     * Constructor initializes the Island with specifications from UserSetupManager or default values.
     */
    public Island() {
        this.width = Optional.of(UserSetupManager.INSTANCE.get().width()).orElse(islandSpecification.width());
        this.height = Optional.of(UserSetupManager.INSTANCE.get().height()).orElse(islandSpecification.height());
        initializeCellGrids();
        super.cells = Arrays.stream(grid).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    /**
     * Returns the type of the territory.
     *
     * @return the type of the territory.
     */
    @Override
    public TerritoryType getType() {
        return ISLAND;
    }


    /**
     * Retrieves the maximum count of residents allowed for a given organism type.
     *
     * @param type the organism type.
     * @return the maximum count of residents allowed for the given organism type.
     */
    public int getMaxResidentCountForOrganismType(OrganismType type) {
        return islandSpecification.organismCapacity().get(type);
    }

    /**
     * Computes a random destination cell from the current cell considering the speed of movement.
     *
     * @param cell the starting cell.
     * @param speed the speed indicating how many cells away from the starting cell the organism can move.
     * @return a randomly determined destination cell.
     */
    @Override
    public Cell getRandomDestination(Cell cell, int speed) {
        Cell destination = getNextDestination(cell);
        return speed < 2 ? destination : getRandomDestination(destination, speed - 1);
    }

    /**
     * Initializes the grid of cells based on width and height.
     */
    private void initializeCellGrids() {
        grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(this, j, i, calculateAvailableDirections(j, i));
            }
        }
    }


    /**
     * Calculates available directions for a cell based on its position.
     *
     * @param x the x-coordinate of the cell.
     * @param y the y-coordinate of the cell.
     * @return an array of available directions.
     */
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

    /**
     * Computes the next destination cell for a given starting cell.
     *
     * @param start the starting cell.
     * @return the next destination cell.
     */
    private Cell getNextDestination(Cell start) {
        Direction nextDirection = RandomGenerator.getRandomDirection(start);
        int x = start.getX() + nextDirection.getX();
        int y = start.getY() + nextDirection.getY();
        return grid[y][x];
    }


}
