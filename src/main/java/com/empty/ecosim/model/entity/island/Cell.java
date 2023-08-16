package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Cell {
    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, List<Organism>> residents = new HashMap<>();
    private Map<OrganismType, Integer> indexOfLastDeadMap = new HashMap<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Map<OrganismType, List<Organism>> getResidentsMap() {
        return residents;
    }

    public List<Organism> getOrganismsByType(OrganismType type) {
        return Optional.ofNullable(residents.get(type))
                .orElse(new ArrayList<>());
    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new ArrayList<>()).add(organism);

    }

    public Stream<Eater> getAllEaters() {
        return residents.values().stream()
                .filter(organisms -> organisms.stream().anyMatch(organism -> organism instanceof Eater))
                .flatMap(Collection::stream)
                .map(a -> (Eater) a);
    }

    public boolean removeResident(Organism organism) {
        List<Organism> residentsOfType = residents.get(organism.getType());
        boolean result = false;
        if (residentsOfType != null) {
            result = residentsOfType.remove(organism);
        }

        if (residentsOfType.size() == 0) {
            residents.remove(organism.getType());
        }

        return result;
    }

    // good
    public Organism getAliveOrganism(OrganismType type) {
        List<Organism> requestedResidents = residents.get(type);

        Integer indexOfLastDead = indexOfLastDeadMap.getOrDefault(type, 0);
        if (indexOfLastDead + 1 < requestedResidents.size()) {
            indexOfLastDeadMap.compute(type, (k, v) -> v == null ? 0 : v + 1);
            return requestedResidents.get(indexOfLastDead);
        }
        return null;
    }

    // good
    public int getResidentCountByType(OrganismType type) {
        List<Organism> residentsOfType = residents.get(type);
        return residentsOfType == null ? 0 : residentsOfType.size();
    }

    // good
    public Set<OrganismType> getPresentOrganismTypes() {
        return new HashSet<>(residents.keySet());
    }

    public void removeDeadOrganisms() {
        residents.values().forEach(organisms -> organisms.removeIf(organism -> !organism.isAlive()));
        residents.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        indexOfLastDeadMap = new HashMap<>();
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
