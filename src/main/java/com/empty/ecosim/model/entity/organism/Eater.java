package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.island.Cell;

public interface Eater {
    boolean findFoodAt(Cell cell);
}
