package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.statistics.StatisticsCollector;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Cell {
    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, List<Organism>> residents = new HashMap<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Map<OrganismType, List<Organism>> getResidentsMap() {
        return residents;
    }

    public void initializeOrganismListByType(OrganismType type) {
        residents.put(type, new ArrayList<>());
    }

    public void addAllResidents(OrganismType type, List<Organism> newcomers) {
        residents.put(type, newcomers);
    }

    public List<Organism> getResidentsCopyByType(OrganismType type) {
        return Optional.ofNullable(residents.get(type))
                .map(ArrayList::new)
                .orElse(new ArrayList<>());
    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new ArrayList<>()).add(organism);

    }

    public Stream<Eater> getAllEaters() {
//        return residents.values().stream()
//                .filter(organisms -> organisms.stream().anyMatch(organism -> organism instanceof Eater))
//                .flatMap(Collection::stream)
//                .map(a -> (Eater) a);
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
    public int handleConsumptionProcess(int amountOfFood, OrganismType typeOfFood) {
        lock.lock();
        int inStock = getResidentCountByType(typeOfFood);
        if (inStock == 0) {
            return 0;
        }

        if (inStock <= amountOfFood) {
            residents.remove(typeOfFood);
            return inStock;
        }

        List<Organism> sublist = residents.get(typeOfFood).subList(0, inStock - amountOfFood);
        residents.put(typeOfFood, sublist);
        lock.unlock();

        return amountOfFood;
    }

    // good
    public Organism getOrganismForConsumption(OrganismType type) {
        if (getResidentCountByType(type) <= 0) {
            return null;
        }

        List<Organism> requestedResidents = residents.get(type);
        StatisticsCollector.registerPredationCount(type);
        return requestedResidents.remove(requestedResidents.size() - 1);
    }

    // good
    public int getResidentCountByType(OrganismType type) {
        List<Organism> residentsOfType = residents.get(type);
        return residentsOfType == null ? 0 : residentsOfType.size();
    }

    // good
    public Set<OrganismType> getPresentTypes() {
        return residents.keySet();
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
