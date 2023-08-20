package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.predators.Wolf;

import java.util.HashSet;
import java.util.Set;

public class FeedingController {

    private final Territory territory;

    public FeedingController(Territory territory) {
        this.territory = territory;
    }

    public void executeFeeding() {
        territory.getCells().parallelStream().forEach(this::processFeedingForCell);
    }

    private void processFeedingForCell(Cell cell) {
        cell.lock();
        for (Set<Organism> organisms : cell.getResidentsMap().values()) {
                feedAllOrganisms(new HashSet<>(organisms), cell);
        }
        cell.unlock();
    }

    private void feedAllOrganisms(Set<Organism> organisms, Cell cell) {

            for (Organism organism : organisms) {
                if (organism instanceof Eater eater && eater.isHungry()) {
                    eater.eat(cell);
                }

                if (organism.isDead()) {
                    cell.remove(organism);
                }
            }
    }
    private boolean isEaters(Set<Organism> organisms) {
        return organisms.stream()
                .anyMatch(organism -> organism instanceof Eater);
    }
}

