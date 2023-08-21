package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
     */
    public void executeMovement() {
        alreadyMovedOrganisms = ConcurrentHashMap.newKeySet();
        try {
            territory.getCells().parallelStream().forEach(this::processCellMovements);
        } catch (Throwable e) {

            System.exit(21);
            System.out.println("MOVEMENT CONTROLLER EXCEPTION");
            e.printStackTrace();
        }
    }

    private void processCellMovements(Cell cell) {
        cell.lock();
        try {
            List<Movable> organismsToMove = cell.getResidentsMap().values().stream().flatMap(Set::stream).filter(Movable.class::isInstance).map(Movable.class::cast).filter(this::shouldBeMoved).toList();

            organismsToMove.forEach(movable -> movable.move(cell));
        } finally {
            cell.unlock();
        }
    }

    private boolean shouldBeMoved(Movable movable) {
        if (alreadyMovedOrganisms.contains(movable) || movable.getSpeed() == 0) {
            return false;
        }

        alreadyMovedOrganisms.add(movable);
        return true;
    }
}
