package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.Animal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovementController {

    private final Territory territory;
//    private Set<Movable> movedOrganisms = new ConcurrentSkipListSet<>();
    private Set<Movable> movedOrganisms;

    /**
     * Creates a new instance of the MovementController for a given territory.
     *
     * @param territory the territory in which organisms will move.
     */
    public MovementController(Territory territory) {
        this.territory = territory;
    }

    /**
     * Executes the movement cycle for the territory.
     */
    public void executeMovement() {
        movedOrganisms = new HashSet<>();
        try {
//            territory.getCells().parallelStream().forEach(this::processCellMovements);
            territory.getCells().forEach(this::processCellMovements);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void processCellMovements(Cell startCell) {
        startCell.lock();
        for (OrganismType type : startCell.getPresentOrganismTypes()) {
            Set<Organism> residents = startCell.getOrganismsByType(type);

            List<Movable> organismsToMove = residents.stream()
                    .filter(Movable.class::isInstance)
                    .map(Movable.class::cast)
                    .filter(this::shouldBeMoved)
                    .toList();

            moveOrganisms(startCell, type, new ArrayList<>(organismsToMove));
        }
        startCell.unlock();
    }

    private void moveOrganisms(Cell currentCell, OrganismType type, List<Movable> organismsToMove) {
        for (Movable movableOrganism : organismsToMove) {
            movableOrganism.move();

            // TODO REDO IT
            if (movableOrganism instanceof Animal animal && animal.isDead()) {
                currentCell.remove(animal);
                return;
            }

            Cell destination = determineDestination(currentCell, movableOrganism.getSpeed());
            destination.lock();
            if (isValidDestination(destination, currentCell, type)) {
                    currentCell.remove((Organism) movableOrganism);
                    destination.addResident((Organism) movableOrganism);
            }
            destination.unlock();
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

        synchronized(movedOrganisms) {
            if (movedOrganisms.contains(movable) || movable.getSpeed() == 0) {
                return false;
            }
            movedOrganisms.add(movable);
        }
        return true;
    }
}
