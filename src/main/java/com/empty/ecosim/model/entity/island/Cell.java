package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

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

    public Set<Organism> getOrganismsByType(OrganismType type) {
        return organisms.getOrDefault(type, Collections.emptySet());
    }

    public Map<OrganismType, Set<Organism>> getResidentsMap() {
        return organisms;
    }


    public Cell chooseRandomAdjasentCell(int speed) {
        Cell destination = territory.getRandomDestination(this, speed);
        return destination == null ? this : destination;
    }

    public boolean canAccommodate(OrganismType type) {
        int availableSpace = territory.getMaximumCapacityFor(type) - getResidentsCountFor(type);
        return availableSpace > 0;
    }

    public void addOrganism(Organism organism) {
        organisms.computeIfAbsent(organism.getType(), k -> new LinkedHashSet<>()).add(organism);
    }

    public Territory.Direction[] getAllowedDirections() {
        return allowedDirections;
    }

    public Optional<Organism> getOrganism(OrganismType type) {
        return organisms.getOrDefault(type, Collections.emptySet()).stream().findAny();
    }

    public int getResidentsCountFor(OrganismType type) {
        return getOrganismsByType(type).size();
    }


    public void removeOrganism(Organism organism) {
        Set<Organism> residentsOfType = organisms.get(organism.getType());
        residentsOfType.remove(organism);
    }

    public Set<OrganismType> currentOrganismTypes() {
        return organisms.keySet();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean tryToLock() {
        return lock.tryLock();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
