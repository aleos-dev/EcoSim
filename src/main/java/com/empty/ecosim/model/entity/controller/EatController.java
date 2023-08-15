package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;

import java.util.List;
public class EatController {

    private final Territory territory;

    public EatController(Territory territory) {
        this.territory = territory;
    }

    public void runEatCycle() {
        territory.getCells().forEach(this::runEatForCell);
    }

    public void runEatForCell(Cell cell) {
        cell.getPresentTypes().stream()
                .map(cell::getResidentsByType)
                .flatMap(List::stream)
                .filter(organism -> organism.isAlive() && organism instanceof Eater)
                .forEach(eater -> ((Eater) eater).eat(cell));

        cell.clearDead();
    }
}
