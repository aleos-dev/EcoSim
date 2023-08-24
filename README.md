# EcoSim: Ecosystem Simulator

EcoSim is a Java-based simulation program that models an island ecosystem with a variety of plants and animals.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)

## Features

- **Dynamic Ecosystem**: Models a grid-based island ecosystem with plants, herbivores, and predators.
- **Realistic Interactions**: Animals can eat, move, reproduce, or die based on the ecosystem's conditions.
- **Statistics**: Provides real-time statistics on the number and types of species, helping users understand the balance of the ecosystem.
- **Multi-threaded**: Uses multi-threading for simultaneous actions and interactions.

## Installation

### Prerequisites:

- JRE (Java Runtime Environment) version 20 or higher. If you haven't installed it yet, download and install from the official [Java website](https://www.oracle.com/java/technologies/javase-jre-downloads.html).

### Installation Steps:

1. Download the `EcoSim-1.0-SNAPSHOT.jar` file.
2. Store the JAR file in a convenient directory on your system.

## Usage

### Configuration:
On the first run, the application will create a configuration directory specific to your OS:

- **Windows**: `C:\Users\[YourUsername]\AppData\Local\EcosystemSimulator(aleos-dev)`
- **Linux/Unix**: `/home/[YourUsername]/.config/EcosystemSimulator(aleos-dev)`
- **Mac**: `/Users/[YourUsername]/Library/Application Support/EcosystemSimulator(aleos-dev)`

Ensure you have the necessary permissions to create and modify files in these directories.

### Run the Simulation:

1. Navigate to the directory containing the `EcoSim-1.0-SNAPSHOT.jar` file.
2. Open a terminal or command prompt.
3. Enter the following command:
   ```bash
   java -jar EcoSim-1.0-SNAPSHOT.jar
