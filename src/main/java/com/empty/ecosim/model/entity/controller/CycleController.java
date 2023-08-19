package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Territory;

import static java.lang.Thread.sleep;

public class CycleController implements Runnable {

    private final FeedingController fc;
    private final MovementController mc;
    private final ReproduceController rc;

    public CycleController(Territory territory) {
        fc = new FeedingController(territory);
        mc = new MovementController(territory);
        rc = new ReproduceController(territory);
    }

    public void runCycle() {
        fc.executeFeeding();
        mc.executeMovement();
        rc.executeReproductionForAnimals();
    }

    @Override
    public void run() {
        runCycle();
    }
}
