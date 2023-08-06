package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;

public class Cell {
    private final Map<OrganismType, Set<Organism>> residents = new HashMap<>();

    public Map<OrganismType, Set<Organism>> getResidents() {
        return residents;
    }

    public <T extends OrganismType> Set<Organism>  getEntitiesFor(T type) {
        return residents.get(type);
    }

    public void addEntity(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new HashSet<>()).add(organism);
    }

    public boolean remove(Organism organism) {
        Set<Organism> entities = residents.get(organism.getType());
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

    @Override
    public String toString() {
        return "Cell{" +
                "map=" + residents +
                '}';
    }
}
