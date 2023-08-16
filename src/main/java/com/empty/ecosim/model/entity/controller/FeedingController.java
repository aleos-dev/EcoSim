package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.ArrayList;
import java.util.List;

public class FeedingController {

    private final Territory territory;

    public FeedingController(Territory territory) {
        this.territory = territory;
    }

    public void executeFeedingCycle() {
        territory.getCells().forEach(this::processCellFeeding);
    }

//    private void processCellFeeding(Cell cell) {
//        cell.getPresentOrganismTypes().stream()
//                .flatMap(type -> cell.getOrganismsByType(type).stream())
//                .forEach(organism -> attemptFeeding(organism, cell));
//
//    }
    private void processCellFeeding(Cell cell) {
        List<OrganismType> organismTypes = new ArrayList<>(cell.getPresentOrganismTypes());
        for (OrganismType type : organismTypes) {
            List<Organism> organisms = new ArrayList<>(cell.getOrganismsByType(type));
            for (Organism organism : organisms) {
                attemptFeeding(organism, cell);
                if (!organism.isAlive()) {
                    cell.remove(organism);
                }
            }
        }
    }

    private void attemptFeeding(Organism organism, Cell cell) {
        if (organism instanceof Eater eater) {
            eater.eat(cell);

            if (!organism.isAlive()) {
                cell.remove(organism);
            }
        }
    }

}

