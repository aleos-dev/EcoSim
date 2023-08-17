package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class FeedingController {

    private final Territory territory;

    public FeedingController(Territory territory) {
        this.territory = territory;
    }

    public void executeFeedingCycle() {
        try (ForkJoinPool customThreadPool = new ForkJoinPool(4)) {
            customThreadPool.submit(() ->
                    territory.getCells().parallelStream().forEach(this::processFeedingForCell)
            ).join();
        }
    }

    private void processFeedingForCell(Cell cell) {
        cell.getResidentsMap().values().stream()
                .filter(this::hasEaters)
                .forEach(organismSet -> feedAllOrganisms(organismSet, cell));
    }

    private void feedAllOrganisms(Set<Organism> organismSet, Cell currentLocation) {
        Iterator<Organism> iterator = organismSet.iterator();
        while (iterator.hasNext()) {
            Organism organism = iterator.next();
            feedOrganism(organism, currentLocation);
            if (!organism.isAlive()) {
                iterator.remove();
            }
        }
    }

    private void feedOrganism(Organism organism, Cell cell) {
        if (organism instanceof Eater eater) {
            eater.eat(cell);
        }
    }

    private boolean hasEaters(Set<Organism> organisms) {
        return organisms.stream()
                .anyMatch(organism -> organism instanceof Eater);
    }
}

