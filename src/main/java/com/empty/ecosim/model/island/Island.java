package com.empty.ecosim.model.island;

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
                matrix[i][j] = new Cell();
            }
        }
    }
}
