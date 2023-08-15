package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.island.Territory;

import java.util.List;


public class EcosystemSimulationController {
    private Territory territory;
    private List<Cell> locations;

    public EcosystemSimulationController(Territory territory) {
        this.territory = territory;
        locations = territory.getCells();
    }

    public void makeTurn() {


    }

    public void doEat() {
        locations.forEach(location -> location.getAllEaters().forEach(s -> s.eat(location)));
    }

    public void doMove() {

    }

}
