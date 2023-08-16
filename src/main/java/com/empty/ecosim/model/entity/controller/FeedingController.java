package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;

public class FeedingController {

    private final Territory territory;

    public FeedingController(Territory territory) {
        this.territory = territory;
    }

    public void executeFeedingCycle() {
        territory.getCells().forEach(this::processCellFeeding);
    }

    private void processCellFeeding(Cell cell) {
        cell.getPresentOrganismTypes().stream()
                .flatMap(type -> cell.getOrganismsByType(type).stream())
                .forEach(organism -> attemptFeeding(organism, cell));

        cell.removeDeadOrganisms();
    }

    private void attemptFeeding(Organism organism, Cell cell) {
        if (organism.isAlive() && organism instanceof Eater eater) {
            eater.eat(cell);
        }
    }
}

