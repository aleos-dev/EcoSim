package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, LinkedHashSet<Organism>> residents = new HashMap<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Map<OrganismType, LinkedHashSet<Organism>> getResidentsMap() {
        return residents;
    }

    public Set<Organism> getOrganismsByType(OrganismType type) {
        return Optional.ofNullable(residents.get(type))
                .orElse(new LinkedHashSet<>());
    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new LinkedHashSet<>()).add(organism);

    }

    public Organism extractAnyOrganismByType(OrganismType type) {
        LinkedHashSet<Organism> organismSet = residents.get(type);

        if (organismSet == null) return null;

        Organism organism = null;
        Iterator<Organism> iterator = organismSet.iterator();
        if (iterator.hasNext()) {
            organism = iterator.next();
            iterator.remove();
        }

        return organism;
    }

    public int getResidentCountByType(OrganismType type) {
        Set<Organism> residentsOfType = residents.get(type);
        return residentsOfType == null ? 0 : residentsOfType.size();
    }


    public void remove(Organism organism) {
        Set<Organism> residentsOfType = residents.get(organism.getType());
        residentsOfType.remove(organism);
    }

    public Set<OrganismType> getPresentOrganismTypes() {
        return new HashSet<>(residents.keySet());
    }

    public boolean hasType(OrganismType type) {
        return residents.containsKey(type);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "map=" + residents +
                '}';
    }

}
