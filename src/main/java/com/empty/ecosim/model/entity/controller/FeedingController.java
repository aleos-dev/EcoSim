package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.empty.ecosim.utils.RandomGenerator.getRandomOrganismType;

public class FeedingController {

    private final Territory territory;

    public FeedingController(Territory territory) {
        this.territory = territory;
    }

    public void executeFeeding() {
//        try (ForkJoinPool customThreadPool = new ForkJoinPool(4)) {
//            customThreadPool.submit(() ->
//                    territory.getCells().parallelStream().forEach(this::processFeedingForCell)
//            ).join();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }

        territory.getCells().parallelStream().forEach(this::processFeedingForCell);
//        territory.getCells().forEach(this::processFeedingForCell);

    }

    private void processFeedingForCell(Cell cell) {
        cell.lock();
        for (Set<Organism> organisms : cell.getResidentsMap().values()) {
            if (isEaters(organisms)) {
                feedAllOrganisms(new HashSet<>(organisms), cell);
            }
        }

//        List<Set<Organism>> clonedOrganismsList = cell.getResidentsMap().values().stream()
//                .map(HashSet::new)
//                .collect(Collectors.toList());
//
//        for (Set<Organism> organisms : clonedOrganismsList) {
//            if (hasEaters(organisms)) {
//                feedAllOrganisms(new HashSet<>(organisms), cell);
//            }
//        }

        cell.unlock();
    }

    private void feedAllOrganisms(Set<Organism> organisms, Cell currentLocation) {

            for (Organism organism : organisms) {
                var availablePreyTypes = filterEdibleTypesInCell((Eater)organism, currentLocation);
                feedOrganism(organism, currentLocation, availablePreyTypes);

                if (!organism.isAlive()) {
                    currentLocation.remove(organism);
                }

//                AnimalFactory factory = new SimpleAnimalFactory();
//                organisms.add(factory.create(AnimalType.BEAR));
            }
    }

    private void feedOrganism(Organism organism, Cell cell, List<OrganismType> availablePreyTypes) {

        Organism food = huntForPrey(cell, availablePreyTypes);

        if (food == null || !((Eater) organism).canCaptureFood(food.getType())) return;
        ((Eater) organism).eat(food);
        cell.remove(food);
    }


    private boolean isEaters(Set<Organism> organisms) {
        return organisms.stream()
                .anyMatch(organism -> organism instanceof Eater);
    }

    private Organism huntForPrey(Cell cell, List<OrganismType> availablePreyTypes) {
        if (availablePreyTypes.isEmpty()) return null;
        var targetType = getRandomOrganismType(availablePreyTypes);

        return cell.getPrey(targetType);

    }

    protected List<OrganismType> filterEdibleTypesInCell(Eater eater, Cell cell) {
        return cell.getPresentOrganismTypes().stream()
                .filter(eater::isEdible)
                .collect(Collectors.toList());
    }
}

