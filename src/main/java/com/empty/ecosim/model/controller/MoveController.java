package com.empty.ecosim.model.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MoveController {

    private final Territory territory;
    private final Set<Movable> alreadyMoved = new HashSet<>(1_000_000);

    public MoveController(Territory territory) {
        this.territory = territory;
    }

    public void run() {
        territory.getCells().forEach(cell -> {
            Arrays.stream(AnimalType.values())
                    .map(cell::getResidentsCopyByType)
                    .filter(Objects::nonNull)
                    .forEach(list -> list.stream()
                            .filter(o -> o instanceof Animal)
                            .filter(Organism::isAlive)
                            .map(o -> (Animal) o)
                            .forEach(animal -> animal.move(territory, cell))
                    );

        });
        territory.finishTravel();
    }


    public void makeMove(Movable organism) {
        // TODO: this should be done another subject
//        if(!organism.isAlive()) {
//            currentCell.removeResidentFromCell(this);
//            StatisticsCollector.registerStarvingCount(this.getType());
//            return;
//        }



        territory.beginTravel(this, currentCell, destinationCell);
    }
    private void moveOrganism(Movable organism, Cell from, Cell to) {
        if (from == to) {
            return;
        }
        from.removeResident(organism);
        to.addResident(organism);
    }

    private Cell generateDestinationLocation(Cell start, int speed) {
        Cell destination = territory.getRandomAdjacentCell(start, speed);

        return destination == null ? start : destination;
    }
}
