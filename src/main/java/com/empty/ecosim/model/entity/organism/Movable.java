package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.island.Cell;

public interface Movable {
    void move(Cell cell);
    int getSpeed();
}
