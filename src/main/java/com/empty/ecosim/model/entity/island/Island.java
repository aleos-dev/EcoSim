package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.empty.ecosim.model.entity.island.TerritoryType.ISLAND;

public class Island extends Territory {

    private final int width;
    private final int height;
    private Cell[][] matrix;
    private final IslandSpecification islandSpecification = TERRITORY_SPECIFICATION.getSpecificationForType(TerritoryType.ISLAND);

    public Island() {
        this.width = islandSpecification.width();
        this.height = islandSpecification.height();

        initializeCellsMatrix();
        super.cells = Arrays.stream(matrix).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    private void initializeCellsMatrix() {
        matrix = new Cell[height][width];

        // Initialize each cell in the matrix, assuming you have an appropriate constructor in Cell class
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                matrix[i][j] = new Cell(i, j);
            }
        }
    }

    @Override
    public void moveOrganismFromSourceToDestination(Organism resident, Cell sourceCell, Cell destinationCell) {
        if (destinationCell.getResidentCountByType(resident.getType()) >= islandSpecification.organismCapacity().get(resident.getType())) {
          return;
        }

        if (sourceCell.removeResidentFromCell(resident)) {
            destinationCell.addResident(resident);
        }
    }

    @Override
    public Cell getRandomPossibleDestination(Cell cell, int speed) {
        int bound = speed * 2 + 1;
        int x = speed - RandomGenerator.getRandomInt(bound);
        bound = Math.abs((speed - Math.abs(x))) * 2 + 1;
        int y = speed - RandomGenerator.getRandomInt(bound);
        return getCellAtCoordinate(x + cell.getX(), y + cell.getY());
    }


    private boolean isCoordinateValid(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        return y >= 0 && y < height;
    }

    private Cell getCellAtCoordinate(int x, int y) {
            if (isCoordinateValid(x, y)) {
                return matrix[y][x];
            }

        return null;
    }


    public Set<OrganismType> getResidentOrganismTypes() {
        return TERRITORY_SPECIFICATION.getSpecificationForType(ISLAND).organismCapacity().keySet();
    }

    public int getMaxResidentCountForOrganismType(OrganismType type) {
        return islandSpecification.organismCapacity().get(type);
    }
}
