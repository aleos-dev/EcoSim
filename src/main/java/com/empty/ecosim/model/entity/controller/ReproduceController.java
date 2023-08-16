package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.statistics.StatisticsCollector;

import java.util.*;


public class ReproduceController {
    private final Territory territory;

    public ReproduceController(Territory territory) {
        this.territory = territory;
    }

    public void initiateReproduction() {
        territory.getCells().forEach(this::runReproduceForCell);
    }

    private void runReproduceForCell(Cell cell) {
        cell.getResidentsMap().forEach((residentType, residents) -> {
            int maxAvailableCapacity = Math.max(territory.getMaximumCapacityFor(residentType) - residents.size(), 0);

            List<Organism> newborns = generateNewborns(residents, maxAvailableCapacity);
            residents.addAll(newborns);

            StatisticsCollector.registerNewbornCount(residentType, newborns.size());
        });
    }

    private List<Organism> generateNewborns(Set<Organism> organisms, int maxCapacity) {
        List<Organism> newborns = new ArrayList<>();

        for (Organism organism : organisms) {
            if (newborns.size() >= maxCapacity) break;
            newborns.addAll(organism.reproduce());
        }

        if (newborns.size() > maxCapacity) {
            newborns.subList(maxCapacity, newborns.size()).clear();
        }

        return newborns;
    }
}

