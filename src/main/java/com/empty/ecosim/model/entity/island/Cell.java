package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Cell {
    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, List<Organism>> residents = new HashMap<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public  List<Organism> getResidentListIfPresent(OrganismType type) {
        try {
            return residents.get(type);
        } finally {
        }
    }
    // Add a generic method to retrieve a specific type of organism list
//    public <T extends Organism> List<T> getResidentListIfPresent(OrganismType type, Class<T> clazz) {
//        List<Organism> organisms = residents.get(type);
//        if (organisms == null) {
//            return null;
//        }
//
//        // Filter and cast the list to the desired type
//        return organisms.stream()
//                .filter(clazz::isInstance)
//                .map(clazz::cast)
//                .toList(); // Requires Java 16+; use .collect(Collectors.toList()) for older versions
//    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new ArrayList<>()).add(organism);

    }

    public List<Organism> getAllOrganismAsList() {
        return residents.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Organism getAnyAndRemove(OrganismType type) {

        var list = residents.get(type);
        if (list == null || list.isEmpty()) {
            return null;
        }

        int size = list.size();

        Organism organism =  list.remove(size - 1);
        return organism;
    }

    public int getResidentNumber(OrganismType type) {
        List<Organism> list = residents.get(type);
        return list == null ? 0 : list.size();
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
