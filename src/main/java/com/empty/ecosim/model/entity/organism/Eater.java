package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.island.Cell;

import java.util.List;

public interface Eater {
    void eat(Cell cell);

    boolean isEdible(OrganismType food);
    boolean isHungry();

    boolean canCaptureFood(OrganismType targetType);

    List<OrganismType> getEdibleTypes();

}
