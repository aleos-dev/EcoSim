package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;

public class Cell {
    private final int x;
    private final int y;
    private final Map<OrganismType, List<Organism>> residents = new HashMap<>();


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Map<OrganismType, List<Organism>> getResidents() {
        return residents;
    }

    public <T extends OrganismType> List<Organism> getResidentIfPresent(T type) {
        return residents.get(type);
    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new ArrayList<>()).add(organism);
    }

    public boolean remove(Organism organism) {
        List<Organism> entities = residents.get(organism.getType());
        if (entities == null) {
            return false;
        }
        entities.remove(organism);
        if (entities.size() == 0) {
            residents.remove(organism.getType());
        }
        return true;
    }

    public boolean isResidentTypePresent(OrganismType residentType) {
        return residents.containsKey(residentType);
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
