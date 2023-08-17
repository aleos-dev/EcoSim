package com.empty.ecosim.model.entity.controller;

import com.empty.ecosim.model.entity.island.Territory;

public class CycleController {

    private final FeedingController fc;
    private final MovementController mc;
    private final ReproduceController rc;

    public CycleController(Territory territory) {
        mc = new MovementController(territory);
        fc = new FeedingController(territory);
        rc = new ReproduceController(territory);
    }

    public void runCycle() {
        fc.executeFeedingCycle();
        mc.executeMovementCycle();
        rc.initiateReproduction();
    }

    public void runCycle(int count) {
        for (int i = 0; i < count; i++) {
            runCycle();
        }
    }
}
