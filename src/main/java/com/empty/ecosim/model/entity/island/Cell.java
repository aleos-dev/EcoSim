package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {

    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, Set<Organism>> residents = new HashMap<>();
    private final Territory.Direction[] possibleDirections;

    public Cell(int x, int y, Territory.Direction[] possibleDirections) {
        this.x = x;
        this.y = y;
        this.possibleDirections = possibleDirections;
    }

    public Map<OrganismType, Set<Organism>> getResidentsMap() {
        return residents;
    }

    public Set<Organism> getOrganismsByType(OrganismType type) {
        return Optional.ofNullable(residents.get(type))
                .orElse(new LinkedHashSet<>());
    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new LinkedHashSet<>()).add(organism);

    }

    public Territory.Direction[] getPossibleDirections() {
        return possibleDirections;
    }

    public Organism extractAnyOrganismByType(OrganismType type) {
        Set<Organism> organismSet = residents.get(type);

        return organismSet == null ? null : extractOrganism(organismSet);
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

    private Organism extractOrganism(Set<Organism> organismSet) {
        Organism organism = null;
        Iterator<Organism> iterator = organismSet.iterator();
        if (iterator.hasNext()) {
            organism = iterator.next();
            iterator.remove();
        }

        return organism;
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
