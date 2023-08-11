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
    private Cell[][] onTravelGrid;
    private final IslandSpecification islandSpecification = TERRITORY_SPECIFICATION.getSpecificationForType(TerritoryType.ISLAND);

    public Island() {
        this.width = islandSpecification.width();
        this.height = islandSpecification.height();


        initializeCellGrids();
        super.cells = Arrays.stream(grid).flatMap(Arrays::stream).collect(Collectors.toList());

    }

    private void initializeCellGrids() {
        grid = new Cell[height][width];
        onTravelGrid = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(j, i);
                onTravelGrid[i][j] = new Cell(j, i);
            }
        }
    }

    @Override
    public void beginTravel(Organism resident, Cell from, Cell to) {
        if (to.getResidentCountByType(resident.getType()) >= islandSpecification.organismCapacity().get(resident.getType())) {
            return;
        }

        if (from.removeResidentFromCell(resident)) {
            onTravelGrid[to.getY()][to.getX()].addResident(resident);

        }
    }

    @Override
    public void finishTravel() {
        Arrays.stream(onTravelGrid).flatMap(Arrays::stream).
                forEach(onTravelCell -> {
                    Arrays.stream(AnimalType.values())
                            .filter(type -> onTravelCell.getResidentCountByType(type) > 0)
                            .forEach(type -> {

                                Cell destinationCell = getCellAtCoordinate(onTravelCell.getX(), onTravelCell.getY());
                                if (destinationCell.getResidentsByType(type) == null) {
                                    destinationCell.initializeOrganismListByType(type);
                                }
                                destinationCell.getResidentsMap().get(type).addAll(onTravelCell.getResidentsByType(type));
                            });
                    onTravelCell.getResidentsMap().clear();
                });
    }

    @Override
    public TerritoryType getType() {
        return ISLAND;
    }


    @Override
    public Cell getRandomPossibleDestination(Cell cell, int speed) {
        int bound = speed * 2 + 1;
        int x = speed - RandomGenerator.getRandomInt(bound);
        bound = Math.abs((speed - Math.abs(x))) * 2 + 1;
        int y = speed - RandomGenerator.getRandomInt(bound);


        Cell newCell = getCellAtCoordinate(x + cell.getX(), y + cell.getY());
        if (newCell != null && newCell.getY() == 20) {
            System.out.println("ALERT");
        }
        return newCell;
    }

    @Override
    public void moveOrganismFromSourceToDestination(Organism resident, Cell sourceCell, Cell destinationCell) {

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




    public Set<OrganismType> getResidentsTypes() {
        return TERRITORY_SPECIFICATION.getSpecificationForType(ISLAND).organismCapacity().keySet();
    }

    public int getMaxResidentCountForOrganismType(OrganismType type) {
        return islandSpecification.organismCapacity().get(type);
    }

}
