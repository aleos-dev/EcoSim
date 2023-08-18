package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovementController {

    private final Territory territory;
    private Set<Movable> movedOrganisms;

    /**
     * Creates a new instance of the MovementController for a given territory.
     * @param territory the territory in which organisms will move.
     */
    public MovementController(Territory territory) {
        this.territory = territory;
    }

    /**
     * Executes the movement cycle for the territory.
     */
    public void executeMovementCycle() {
        movedOrganisms = new HashSet<>();
        territory.getCells().forEach(this::processCellMovements);
    }

    private void processCellMovements(Cell startCell) {
        for (OrganismType type : startCell.getPresentOrganismTypes()) {
            Set<Organism> residents = startCell.getOrganismsByType(type);

            List<Movable> organismsToMove = residents.stream()
                    .filter(Movable.class::isInstance)
                    .map(Movable.class::cast)
                    .filter(this::shouldBeMoved)
                    .toList();

            moveOrganisms(startCell, type, organismsToMove);
        }
    }

    private void moveOrganisms(Cell currentCell, OrganismType type, List<Movable> organismsToMove) {
        for (Movable movableOrganism : organismsToMove) {
            movableOrganism.move();

            // TODO REDO IT
            if (movableOrganism instanceof Animal animal && !animal.isAlive()) {
                currentCell.remove(animal);
                return;
            }

            Cell destination = determineDestination(currentCell, movableOrganism.getSpeed());
            if (isValidDestination(destination, currentCell, type)) {
                currentCell.remove((Organism) movableOrganism);
                destination.addResident((Organism) movableOrganism);
            }
        }
    }

    private boolean isValidDestination(Cell destination, Cell currentCell, OrganismType type) {
        return destination != currentCell && canBeAccommodatedAtDestination(destination, type);
    }

    private boolean canBeAccommodatedAtDestination(Cell destination, OrganismType type) {
        int availableSpace = territory.getMaximumCapacityFor(type) - destination.getResidentCountByType(type);
        return availableSpace > 0;
    }

    private Cell determineDestination(Cell currentCell, int speed) {
        Cell destination = territory.getRandomDestination(currentCell, speed);
        return destination == null ? currentCell : destination;
    }

    private boolean shouldBeMoved(Movable movable) {
        if (movedOrganisms.contains(movable) || movable.getSpeed() == 0) {
            return false;
        }
        movedOrganisms.add(movable);
        return true;
    }
}
