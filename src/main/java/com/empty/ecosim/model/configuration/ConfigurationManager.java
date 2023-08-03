package com.empty.ecosim.model.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.empty.ecosim.model.configuration.ConfigurationManager.ResourceType.ANIMAL;
import static com.empty.ecosim.model.configuration.ConfigurationManager.ResourceType.PLANT;

public class ConfigurationManager {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();
    private static final String INTERNAL_ANIMAL_RESOURCE_PATH = "com/empty/ecosim/model/animals/animalsSpec.json";
    private static final String INTERNAL_PLANT_RESOURCE_PATH = "com/empty/ecosim/model/plants/plantsSpec.json";
    private static final String APP_NAME = "Eco-Sim";

    public enum ResourceType { ANIMAL, PLANT, CELL}
    private final Map<ResourceType, Path> resources = new HashMap<>();


    private ConfigurationManager() {
        Path pathToConfigFolder = getPathToConfigFolder();

        try {
            Files.createDirectories(pathToConfigFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directories for path: " + pathToConfigFolder, e);
        }

        Path pathToAnimalsSpecificationResource = Paths.get(pathToConfigFolder.toString(), "animalsSpec.json");
        ensureConfigFileExists(pathToAnimalsSpecificationResource, INTERNAL_ANIMAL_RESOURCE_PATH);
        resources.put(ANIMAL, pathToAnimalsSpecificationResource);

        Path pathToPlantsSpecificationResource = Paths.get(pathToConfigFolder.toString(), "plantsSpec.json");
        ensureConfigFileExists(pathToPlantsSpecificationResource, INTERNAL_PLANT_RESOURCE_PATH);
        resources.put(PLANT, pathToPlantsSpecificationResource);

        Path pathToCellSpecificationResource = Paths.get(pathToConfigFolder.toString(), "cellSpec.json");

    }

    public String getResource(ResourceType resourceType) throws IOException {
        return Files.readString(resources.get(resourceType));
    }

    public void saveResource(ResourceType resourceType, String resource) throws IOException {
        Files.writeString(resources.get(resourceType), resource);
    }

    private void ensureConfigFileExists(Path pathToFile, String internalResourcePath) {
        if (!Files.exists(pathToFile)) {
            try (InputStream inputStream = loadResourceAsStream(internalResourcePath)){

                    Files.write(pathToFile, inputStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to " + pathToFile, e);
            }
        }
    }

    private static Path getPathToConfigFolder() {
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        String homeDir = System.getProperty("user.home");
        Path pathToConfigFolder;

        if (operatingSystem.contains("win")) {
            pathToConfigFolder = Paths.get(homeDir, "AppData", "Local", APP_NAME);
        } else if (operatingSystem.contains("nux") || operatingSystem.contains("nix")) {
            pathToConfigFolder = Paths.get(homeDir, ".config", APP_NAME);
        } else if (operatingSystem.contains("mac")) {
            pathToConfigFolder = Paths.get(homeDir, "Library", "Application Support", APP_NAME);
        } else {
            throw new UnsupportedOperationException("Unsupported operating system");
        }
        return pathToConfigFolder;
    }

    private InputStream loadResourceAsStream(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileNotFoundException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
