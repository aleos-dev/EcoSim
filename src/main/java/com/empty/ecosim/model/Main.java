package com.empty.ecosim.model;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {

        EcosystemSimulator simulator = new EcosystemSimulator();

        for (int i = 0; i < 300; i++) {
            simulator.runCycle();

            simulator.printStatistic();
        }
    }

}
