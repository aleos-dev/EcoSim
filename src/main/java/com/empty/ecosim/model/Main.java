package com.empty.ecosim.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
        EcosystemSimulator simulator = new EcosystemSimulator(executor);
        simulator.start();
//        for (int i = 0; i < 15; i++) {
//
//
//            simulator.runCycle();
//
//            simulator.printStatistic();
//        }
    }

}
