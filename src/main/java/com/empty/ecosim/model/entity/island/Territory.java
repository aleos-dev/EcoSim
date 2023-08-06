package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;

import java.util.ArrayList;
import java.util.List;

public abstract class Territory {

    List<Cell> cells = new ArrayList<>();


//    public abstract void clean();
    public abstract Cell getAdjasentCellAtDirection(Cell cell, Direction direction);

    public abstract void moveResidentFromTo(Organism resident, Cell sourceCell, Cell destinationCell);

}
