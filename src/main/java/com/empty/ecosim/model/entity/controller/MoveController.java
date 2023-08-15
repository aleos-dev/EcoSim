package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveController {

    private final Territory territory;
    private Set<Movable> alreadyMovedOrganisms = new HashSet<>();

    public MoveController(Territory territory) {
        this.territory = territory;
    }

    public void runMoveCycle() {
        territory.getCells().forEach(this::runMoveForCell);
        alreadyMovedOrganisms = new HashSet<>();
    }

    public void runMoveForCell(Cell cell) {
        for (OrganismType type : cell.getPresentTypes()) {
            List<Organism> residents = cell.getResidentsByType(type);

            for (int i = 0; i < residents.size(); i++) {
                Organism resident = residents.get(i);
                if (resident instanceof Movable movableOrganism) {
                    if (!alreadyMovedOrganisms.contains(movableOrganism) && movableOrganism.move(territory, cell)) {
                        i--;
                        alreadyMovedOrganisms.add(movableOrganism);
                    }
                } else {
                    break;
                }
            }
        }
        cell.clearDead();
    }
}
