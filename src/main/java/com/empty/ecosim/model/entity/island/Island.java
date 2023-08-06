package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Island extends Territory {
    private int width;
    private int height;
    private Cell[][] matrix;


    public Island(int width, int height) {
        this.width = width;
        this.height = height;

        initializeMatrix();
        super.cells = Arrays.stream(matrix).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    private void initializeMatrix() {
        matrix = new Cell[height][width];

        // Initialize each cell in the matrix, assuming you have an appropriate constructor in Cell class
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                matrix[i][j] = new Cell(j, i);
            }
        }
    }

    @Override
    public void moveResidentFromTo(Organism resident, Cell sourceCell, Cell destinationCell) {
        if (sourceCell.remove(resident)) {
            destinationCell.addResident(resident);
        }

    }

    @Override
    public Cell getAdjasentCellAtDirection(Cell cell, Direction direction) {
        int x = cell.getX();
        int y = cell.getY();
        x = x + x * direction.x;
        y = y + y * direction.y;

        return getCell(x, y);
    }


    private boolean isCoordinateValid(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        return y >= 0 && y < height;
    }

    private Cell getCell(int x, int y) {
        if(isCoordinateValid(x, y)) {
            return matrix[x][y];
        }
        return null;
    }
}
