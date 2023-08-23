package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller responsible for managing the movement of organisms
 * within a given territory.
 */
public class MovementController {

    private final Territory territory;
    private Set<Movable> alreadyMovedOrganisms = ConcurrentHashMap.newKeySet();

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
     * This ensures that each organism moves once and only once within a cycle.
     */
    public void executeMovement() {
        alreadyMovedOrganisms = ConcurrentHashMap.newKeySet();
        territory.getCells().parallelStream().forEach(this::processCellMovements);
    }

    /**
     * Processes movements for all {@link Movable} organisms in a specific cell.
     * If the organism hasn't moved in the current cycle and its speed is greater than zero,
     * it is eligible to move.
     *
     * @param cell the cell whose residents are to be moved.
     */
    private void processCellMovements(Cell cell) {
        cell.lock();
        try {
            List<Movable> organismsToMove = cell.getResidentsMap().values().stream().flatMap(Set::stream).filter(Movable.class::isInstance).map(Movable.class::cast).filter(this::shouldBeMoved).toList();

            organismsToMove.forEach(movable -> movable.move(cell));
        } finally {
            cell.unlock();
        }
    }

    /**
     * Determines if a {@link Movable} organism should be moved in the current cycle.
     * If the organism has already moved or its speed is zero, it will not be moved.
     *
     * @param movable the organism to check.
     * @return true if the organism should be moved, false otherwise.
     */
    private boolean shouldBeMoved(Movable movable) {
        if (alreadyMovedOrganisms.contains(movable) || movable.getSpeed() == 0) {
            return false;
        }
        alreadyMovedOrganisms.add(movable);
        return true;
    }
}
