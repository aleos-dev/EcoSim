package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;

import java.util.HashSet;
import java.util.Set;


/**
 * Controller responsible for handling the feeding logic for organisms
 * within a given territory.
 */
public class FeedingController {

    private final Territory territory;

    /**
     * Constructs a new FeedingController for the specified territory.
     *
     * @param territory the territory to be managed by this controller.
     */
    public FeedingController(Territory territory) {
        this.territory = territory;
    }

    /**
     * Executes the feeding logic for all organisms in the territory.
     */
    public void executeFeeding() {
        try {
            territory.getCells().parallelStream().forEach(this::processFeedingForCell);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the feeding logic for the organisms within a specific cell.
     *
     * @param cell the cell to process.
     */
    private void processFeedingForCell(Cell cell) {
        cell.lock();
        for (Set<Organism> organisms : cell.getResidentsMap().values()) {
            feedAllOrganisms(new HashSet<>(organisms), cell);
        }
        cell.unlock();
    }

    /**
     * Executes the feeding operation for each organism in the provided set.
     * If an organism is an instance of {@link Eater}, it will attempt to eat
     * from the specified cell. If the organism is dead after attempting to eat,
     * it will be removed from the cell.
     *
     * @param organisms a set of organisms to process.
     * @param cell      the cell where the organisms are located.
     */
    private void feedAllOrganisms(Set<Organism> organisms, Cell cell) {

        for (Organism organism : organisms) {
            if (organism instanceof Eater eater) {
                eater.eat(cell);
            } else {
                break;
            }

            if (organism.isDead()) {
                cell.removeOrganism(organism);
            }
        }
    }
}

