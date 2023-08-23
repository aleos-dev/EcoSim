package com.empty.ecosim.model;

import com.empty.ecosim.model.configuration.userSetup.UserSetupManager;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents a controller for the {@link EcosystemSimulator}, providing mechanisms to
 * start and manage the simulation using a scheduled executor service.
 */
public class EcosystemSimulatorController {
    ScheduledExecutorService executor;

    public EcosystemSimulatorController() {
        executor = Executors.newScheduledThreadPool(4);
    }

    /**
     * Starts the ecosystem simulation.
     * <p>
     * The simulation is started based on user input and can be stopped manually
     * after a predefined time. The user is then prompted for further actions.
     * </p>
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        EcosystemSimulator simulator = new EcosystemSimulator(executor);

        while (true) {
            executor.execute(simulator::start);

            executor.schedule(simulator::stop,
                    UserSetupManager.INSTANCE.get().timeToExecuteSimulation(),
                    TimeUnit.SECONDS);

            String userInput = scanner.nextLine();
            if (!Objects.equals(userInput, "y")) {
                executor.shutdown();
                break;
            }
        }
    }
}
