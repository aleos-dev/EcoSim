package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MovementController {

    private final Territory territory;
    private Set<Movable> movedOrganisms = new HashSet<>();

    public MovementController(Territory territory) {
        this.territory = territory;
    }

    public void executeMovementCycle() {
        territory.getCells().forEach(this::processCellMovements);
        movedOrganisms = new HashSet<>();
    }

    private void processCellMovements(Cell startCell) {
        for (OrganismType type : startCell.getPresentOrganismTypes()) {
            Set<Organism> residents = startCell.getOrganismsByType(type);

            Iterator<Organism> iterator = residents.iterator();
            while (iterator.hasNext()) {
                Organism organism = iterator.next();
                if (organism instanceof Movable movable && movable.getSpeed() != 0) {
                    if(shouldMove(movable)){
                        movable.move();
                        if(!organism.isAlive()) {
                            iterator.remove();
                            continue;
                        }
                        Cell destination = getDestination(startCell, movable.getSpeed());
                        if (destination == startCell) {
                            continue;
                        }
                        if (territory.getMaximumCapacityFor(type) - destination.getResidentCountByType(type) > 0) {
                            iterator.remove();
                            destination.addResident(organism);
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    private Cell getDestination(Cell startCell, int speed) {
        Cell destination = territory.getRandomAdjacentCell(startCell, speed);


        return destination == null ? startCell : destination;
    }

    private boolean shouldMove(Movable movable) {
        if (movedOrganisms.contains(movable)) {
            return false;
        }
        movedOrganisms.add(movable);
        return true;
    }

}
