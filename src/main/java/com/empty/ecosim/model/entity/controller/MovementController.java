package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovementController {

    private final Territory territory;
    private Set<Movable> movedOrganisms = new HashSet<>();

    public MovementController(Territory territory) {
        this.territory = territory;
    }

    public void executeMovementCycle() {
        territory.getCells().forEach(this::processCellMovements);
        movedOrganisms = new HashSet<>();
    }

    /*  private void processCellMovements(Cell cell) {
          for (OrganismType type : cell.getPresentOrganismTypes()) {
              List<Organism> residents = cell.getOrganismsByType(type);

              for (int i = 0; i < residents.size(); i++) {
                  Organism resident = residents.get(i);
                  if (resident instanceof Movable movableOrganism) {
                      if (!movedOrganisms.contains(movableOrganism) && movableOrganism.move(territory, cell)) {
                          i--;
                          movedOrganisms.add(movableOrganism);
                      }
                  } else {
                      break;
                  }
              }
          }
          cell.removeDeadOrganisms();
      }*/
    private void processCellMovements(Cell startCell) {
        for (OrganismType type : startCell.getPresentOrganismTypes()) {
            List<Organism> residents = startCell.getOrganismsByType(type);

            for (Organism resident : new ArrayList<>(residents)) {
                if (resident instanceof Movable movable && shouldMove(movable)) {
                    int speed = movable.move();
                    Cell destination = getDestination(startCell, speed);
                    if (destination == startCell) {
                        continue;
                    }
                        residents.remove(resident);
                    destination.addResident(resident);
//                    territory.travelFromTo(movable, startCell, destination);
                    movedOrganisms.add(movable);
                } else {
                    break;
                }
            }
        }
        startCell.removeDeadOrganisms();
    }

    private Cell getDestination(Cell startCell, int speed) {
        Cell destination = territory.getRandomAdjacentCell(startCell, speed);

        return destination == null ? startCell : destination;
    }

    private boolean shouldMove(Movable movable) {
        return !movedOrganisms.contains(movable);
    }

}
