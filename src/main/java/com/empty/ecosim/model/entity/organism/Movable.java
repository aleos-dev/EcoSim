package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;

public interface Movable {
    void move(Territory territory, Cell currentCell);
}
