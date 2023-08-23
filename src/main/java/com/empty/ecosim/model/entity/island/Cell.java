package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a cell within a territory. The cell contains organisms and maintains synchronization mechanisms
 * for concurrent access.
 */
public class Cell {

    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, Set<Organism>> organisms = new ConcurrentHashMap<>();
    private final Territory.Direction[] allowedDirections;
    private final Territory territory;

    public Cell(Territory territory, int x, int y, Territory.Direction[] allowedDirections) {
        this.territory = territory;
        this.x = x;
        this.y = y;
        this.allowedDirections = allowedDirections;
    }

    /**
     * Retrieves all organisms of a given type present in the cell.
     *
     * @param type The type of organisms to retrieve.
     * @return A set containing the organisms of the specified type.
     */
    public Set<Organism> getOrganismsByType(OrganismType type) {
        return organisms.getOrDefault(type, Collections.emptySet());
    }

    /**
     * Retrieves a map of all organisms grouped by their types present in the cell.
     *
     * @return A map of organism types to their sets.
     */
    public Map<OrganismType, Set<Organism>> getResidentsMap() {
        return organisms;
    }


    /**
     * Chooses a random adjacent cell based on the given speed.
     *
     * @param speed The speed which might influence the choice of destination.
     * @return The chosen adjacent cell or the current cell if none is found.
     */
    public Cell chooseRandomAdjasentCell(int speed) {
        Cell destination = territory.getRandomDestination(this, speed);
        return destination == null ? this : destination;
    }

    /**
     * Determines whether the cell can accommodate an organism of the given type.
     *
     * @param type The type of organism.
     * @return True if the cell has space for the organism, otherwise false.
     */
    public boolean canAccommodate(OrganismType type) {
        int availableSpace = territory.getMaximumCapacityFor(type) - getResidentsCountFor(type);
        return availableSpace > 0;
    }

    /**
     * Adds an organism to the cell.
     *
     * @param organism The organism to add.
     */
    public void addOrganism(Organism organism) {
        organisms.computeIfAbsent(organism.getType(), k -> new LinkedHashSet<>()).add(organism);
    }

    /**
     * Retrieves the directions allowed for this cell.
     *
     * @return An array of allowed directions.
     */
    public Territory.Direction[] getAllowedDirections() {
        return allowedDirections;
    }

    /**
     * Retrieves a single organism of the specified type if present in the cell.
     *
     * @param type The type of organism.
     * @return An optional containing the organism or empty if not found.
     */
    public Optional<Organism> getOrganism(OrganismType type) {
        return organisms.getOrDefault(type, Collections.emptySet()).stream().findAny();
    }

    /**
     * Counts the number of organisms of a given type present in the cell.
     *
     * @param type The type of organisms to count.
     * @return The number of organisms of the specified type.
     */
    public int getResidentsCountFor(OrganismType type) {
        return getOrganismsByType(type).size();
    }

    /**
     * Removes an organism from the cell.
     *
     * @param organism The organism to remove.
     */
    public void removeOrganism(Organism organism) {
        Set<Organism> residentsOfType = organisms.get(organism.getType());
        residentsOfType.remove(organism);
    }

    /**
     * Retrieves the types of organisms currently present in the cell.
     *
     * @return A set of organism types.
     */
    public Set<OrganismType> currentOrganismTypes() {
        return organisms.keySet();
    }

    /**
     * Acquires the lock for this cell.
     */
    public void lock() {
        lock.lock();
    }

    /**
     * Releases the lock for this cell.
     */
    public void unlock() {
        lock.unlock();
    }

    /**
     * Attempts to acquire the lock for this cell without waiting.
     *
     * @return True if the lock was acquired, otherwise false.
     */
    public boolean tryToLock() {
        return lock.tryLock();
    }

    /**
     * Retrieves the X-coordinate of the cell.
     *
     * @return The X-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the Y-coordinate of the cell.
     *
     * @return The Y-coordinate.
     */
    public int getY() {
        return y;
    }
}
