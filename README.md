# EcoSim: Ecosystem Simulator

EcoSim is a Java-based simulation program that brings to life an island ecosystem populated with a diverse array of flora and fauna.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Program Flow](#Program-Flow)
- [Experience](#experience)

## Features

- **Dynamic Ecosystem**: Seamlessly replicates a grid-oriented island ecosystem packed with various plants, herbivores, and predators.
- **Versatile Configuration Handling**:
   - Recognizes the operating system, tailoring configuration paths for optimal operation.
   - Offers extensive compatibility with Windows, Linux, Unix, and Mac.
- **Resource Management**:
   - Proficiently manages diverse resource categories. 
   - Simplifies the reading of configuration data, resorting to built-in configurations when needed.
- **Realistic Interactions**: Within the simulation, flora and fauna consume, migrate, reproduce, and can even succumb based on the prevailing ecosystem dynamics.
- **Statistics**: Provides real-time statistics, detailing species quantity and variety, aiding in the user's understanding of ecosystem equilibrium.
- **Multithreaded Execution**: Utilizes multi-threading for synchronized tasks and interactions, guaranteeing peak performance.

## Installation

### Prerequisites:

- JRE (Java Runtime Environment) version 20 or above. If it's not already installed, download it from the official Java website.

### Installation Steps:

1. Obtain the `EcoSim.jar` file.
2. Position the JAR file in your desired system directory.

## Usage

### Configuration:

On the initial application run, a configuration directory specific to your OS is set up:

- **Windows**: `C:\Users\[YourUsername]\AppData\Local\EcosystemSimulator(aleos-dev)`
- **Linux/Unix**: `/home/[YourUsername]/.config/EcosystemSimulator(aleos-dev)`
- **Mac**: `/Users/[YourUsername]/Library/Application Support/EcosystemSimulator(aleos-dev)`

Ensure you have adequate permissions to generate and modify files in these directories.

### Run the Simulation:

1. Traverse to the directory containing the `EcoSim.jar` file.
2. Open a terminal or command prompt window.
3. Type and run:
   ```bash
   java -jar EcoSim.jar

   ```
   ![startCycleEsoSim](https://github.com/JR-Abstract/aleos-module-2-EcoSim/assets/121808229/bde6c400-daba-49f9-89e6-ff4868ca2521)
   ![endCycleEcoSim](https://github.com/JR-Abstract/aleos-module-2-EcoSim/assets/121808229/7f49f170-9f5e-478a-97f1-03679780124e)
   ![configEcoSim](https://github.com/JR-Abstract/aleos-module-2-EcoSim/assets/121808229/5929ab67-44e9-4140-b718-d3b9250755f8)



## Program Flow

- **Initialization**:
   - Boots up with the initialization of the `ConfigurationManager` singleton instance.
   - Assures the presence of the configuration directory based on the identified OS and lays out resource paths for each resource type.

- **Resource Handling**:
   - Each resource type defaults to the bundled resource if the configuration file is absent in the predicted location.

- **User Configuration Loading**:
   - At simulation onset or resumption, the `reloadUserConfiguration()` method revisits the `config.yaml` file.
   - Captures ecosystem grid dimensions, organism counts, fertility periods, plant growth thresholds, statistic print intervals, and total simulation runtime. 
   - Guarantees user-defined configuration changes are effectively integrated into the current simulation.

- **Starting Simulation**:
   - The `start()` method reinitiates user configuration.
   - Launches various timed executors, such as the main executor for simulation operations, a statistic collector, and a plant growth driver.

- **Multithreading**:
   - Amplifies performance through efficient multithreading.
   - Uses `parallelStream()` for concurrent feeding, movement, and plant growth operations across different cells.
   - Employs synchronization tools like locks for thread safety, especially when updating common resources.

- **Stopping Simulation**:
   - The `stop()` method halts all executors, pausing the simulation and offering users a chance to modify configurations if desired.

- **Logic Handling**:
   - **Feeding**: Executed in the `FeedingController`, iterating over territory cells.
   - **Movement**: Handled in the `MovementController`, ensuring unique movement for organisms.
   - **Reproduction & Growth**: Located in the `ReproduceController`, managing animal reproduction and plants grow over time in parallel.

- **Territory Population Generation**:
   - Leverages `OrganismSuperFactory` to initiate animal and plant instances.
   - Densely populates each territory cell with a blend of organisms.

## Experience

Reflection on Learning from the EcoSim Project:

Working on the EcoSim project has been an educational journey, one filled with both challenges and revelations. Here are some highlights:

1. **Multithreading Mastery**: Immersing into the realm of multithreading, I explored ScheduledPools and executors. Their power was evident in the simultaneous actions they enabled, demanding precision and deep understanding.

2. **Deadlocks and Concurrent Modifications**: Encountering occasional deadlocks and the infamous ConcurrentModificationException were challenging moments. They were stern reminders of thread safety's importance and the necessity for diligent code design.

3. **Data Serialization and Configuration**: My venture into data serialization and deserialization spanned JSON and YAML. Managing configurations for users and ecosystem entities added versatility to our simulation and emphasized adaptable software design's essence.

4. **Deep Dive into OOP**: This project was a profound exploration of Object-Oriented Programming (OOP). From entities to controllers, OOP's principles permeated every facet, fortifying my appreciation for structured and modular coding.

5. **Java Stream API**: Working on this project also provided me with a unique opportunity to delve deeper into Java's Stream API. Whether it was processing collections, filtering data, or implementing transformations, the Stream API proved to be an invaluable tool that enhanced code readability and efficiency.

6. **Resource Management**: Delving into multiple resource types underscored meticulous resource planning and execution's criticality.

EcoSim was more than a project; it was an enriching learning experience. It honed my skills as a coder, bolstered my problem-solving abilities, and reignited my fervor for software development.

Acknowledgements

I would like to express my profound gratitude to our mentor Alexander and course JavaRush. JavaRush's intense training and challenges directly prepared me to handle this project's complexities. The success I've achieved with EcoSim is largely attributed to the lessons learned from them.
