package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Arrays;
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

    private void initializeCellGrids() {
        grid = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(j, i);
            }
        }
    }

    @Override
    public TerritoryType getType() {
        return ISLAND;
    }

    @Override
    public Cell getRandomAdjacentCell(Cell cell, int speed) {

        int bound = speed * 2 + 1;
        int x = speed - RandomGenerator.getRandomInt(bound);
        bound = Math.abs((speed - Math.abs(x))) * 2 + 1;
        int y = Math.abs(x) - RandomGenerator.getRandomInt(bound);

        return getCellAtCoordinate(x + cell.getX(), y + cell.getY());
    }

    private Cell getCellAtCoordinate(int x, int y) {
        if (isCoordinateValid(x, y)) {
            return grid[y][x];
        }

        return null;
    }

    private boolean isCoordinateValid(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        return y >= 0 && y < height;
    }

    public int getMaxResidentCountForOrganismType(OrganismType type) {
        return islandSpecification.organismCapacity().get(type);
    }

}
