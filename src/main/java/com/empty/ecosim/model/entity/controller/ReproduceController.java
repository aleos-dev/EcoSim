package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.statistics.StatisticsCollector;

import java.util.ArrayList;
import java.util.List;

public class ReproduceController {
    private final Territory territory;

    public ReproduceController(Territory territory) {
        this.territory = territory;
    }

    public void runReproduceCycle() {
        territory.getCells().forEach(this::runReproduceForCell);
    }

    private void runReproduceForCell(Cell cell) {
        cell.getResidentsMap().forEach((residentType, residents) -> {
            int maxAvailableCapacity = territory.getMaximumCapacityFor(residentType) - residents.size();
            if (maxAvailableCapacity <= 0) return;

            reproduceResidents(residents, maxAvailableCapacity);
        });
    }

    private void reproduceResidents(List<Organism> residents, int maxCapacity) {
        List<Organism> newborns = new ArrayList<>(maxCapacity);

        for (Organism resident : residents) {
            newborns.addAll(resident.reproduce());

            if (newborns.size() > maxCapacity) {
                newborns = newborns.subList(0, maxCapacity);
                break;
            }
        }
        mergeResidentsWithNewborn(residents, newborns);
    }

    private void mergeResidentsWithNewborn(List<Organism> residents, List<Organism> newborns) {
        if (!newborns.isEmpty()) {
            Organism representativeOrganism = newborns.get(0);
            residents.addAll(newborns);
            StatisticsCollector.registerNewbornCount(representativeOrganism.getType(), newborns.size());
        }
    }
}
