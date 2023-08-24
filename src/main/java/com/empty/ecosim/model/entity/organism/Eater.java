package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.island.Cell;


public interface Eater {
    void eat(Cell cell);

    boolean isEdible(OrganismType food);

}
