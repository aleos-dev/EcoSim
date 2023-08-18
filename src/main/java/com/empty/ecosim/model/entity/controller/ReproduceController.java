package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.Reproducible;
import com.empty.ecosim.statistics.StatisticsCollector;

import java.util.*;
import java.util.stream.Collectors;


public class ReproduceController {
    private final Territory territory;

    public ReproduceController(Territory territory) {
        this.territory = territory;
    }

    public void initiateReproduction() {
        territory.getCells().parallelStream().forEach(this::runReproduceForCell);
    }

    private void runReproduceForCell(Cell cell) {
        cell.getResidentsMap().entrySet().stream()
                .filter(this::isReproducible)
                .forEach(this::generateNewborns);

    }

    private void generateNewborns(Map.Entry<OrganismType, Set<Organism>> entry) {
        var residentType = entry.getKey();
        var residentSet = entry.getValue();
        int maxCapacity = Math.max(territory.getMaximumCapacityFor(residentType) - residentSet.size(), 0);

        Set<Organism> offspring = residentSet.stream()
                .flatMap(organism -> organism.reproduce().stream())
                .limit(maxCapacity)
                .collect(Collectors.toSet());

        entry.getValue().addAll(offspring);
        StatisticsCollector.registerNewbornCount(residentType, offspring.size());
        StatisticsCollector.increasePopulationCount(residentType, offspring.size());
    }

    // TODO: SCHEDULED POOL FOR GRASS
    private boolean isReproducible(Map.Entry<OrganismType, Set<Organism>> organisms) {
        return organisms.getValue().stream()
                .anyMatch(organism -> organism instanceof Reproducible);
    }
}

