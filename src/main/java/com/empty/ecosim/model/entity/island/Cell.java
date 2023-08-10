package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Cell {
    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<OrganismType, List<Organism>> residents = new HashMap<>();
    private int dieOfHunger;


    private int becameFood;

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

    public List<Organism> getResidentsByType(OrganismType type) {
        return Optional.ofNullable(residents.get(type))
                .map(ArrayList::new)
                .orElse(null);
    }

    public void addResident(Organism organism) {
        residents.computeIfAbsent(organism.getType(), k -> new ArrayList<>()).add(organism);

    }

    public int getAndRemoveNumberOfResidentsByType(int number, OrganismType type) {
        lock.lock();
        int inStock = getResidentCountByType(type);
        if (inStock == 0) {
            return 0;
        }

        if (inStock <= number) {
            residents.remove(type);
            return inStock;
        }

        List<Organism> sublist = residents.get(type).subList(0, inStock - number);
        residents.put(type, sublist);
        lock.unlock();

        return number;
    }

   public List<? extends Organism> getAllResidents() {
    return residents.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
}

    public Organism retrieveAndRemoveAnyResidentByType(OrganismType type) {
        List<Organism> residentsOfType = residents.get(type);
        if (residentsOfType == null || residentsOfType.isEmpty()) {
            return null;
        }
        becameFood++;
        return residentsOfType.remove(residentsOfType.size() - 1);
    }

    public int getResidentCountByType(OrganismType type) {
        List<Organism> residentsOfType = residents.get(type);
        return residentsOfType == null ? 0 : residentsOfType.size();
    }


    public boolean removeResidentFromCell(Organism organism) {
        List<Organism> residentsOfType = residents.get(organism.getType());
        if (residentsOfType == null) {
            return false;
        }

        residentsOfType.remove(organism);
        if (residentsOfType.size() == 0) {
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

    public int getDieOfHunger() {
        return dieOfHunger;
    }

    public int getBecameFood() {
        return becameFood;
    }

    public void setDieOfHunger(int dieOfHunger) {
        this.dieOfHunger = dieOfHunger;
    }

    public void setBecameFood(int becameFood) {
        this.becameFood = becameFood;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "map=" + residents +
                '}';
    }
}
