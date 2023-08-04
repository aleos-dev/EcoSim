package com.empty.ecosim.model.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    public enum ResourceType {
        ANIMAL("animalsSpec.json", "com/empty/ecosim/model/animals/animalsSpec.json"),
        PLANT("plantsSpec.json", "com/empty/ecosim/model/plants/plantsSpec.json");
        //        CELL("cellsSpec.json", );
        private final String configFileName;
        private final String defaultResourcePath;

        ResourceType(String configFileName, String defaultResourcePath) {
            this.configFileName = configFileName;
            this.defaultResourcePath = defaultResourcePath;
        }

        public String getConfigFileName() {
            return configFileName;
        }

        public String getDefaultResourcePath() {
            return defaultResourcePath;
        }
    }

    public static final ConfigurationManager INSTANCE = new ConfigurationManager();

    private static final String APP_NAME = "Eco-Sim";

    private static Path configFolderPath;

    private final Map<ResourceType, Path> resourceFileMapping = new HashMap<>();


    private ConfigurationManager() {
        ensureConfigDirectoryExists();

        Arrays.stream(ResourceType.values()).forEach(this::setupResourcePath);
    }

    private void setupResourcePath(ResourceType resourceType) {

        Path filePath = Paths.get(configFolderPath.toString(), resourceType.getConfigFileName());
        createFileIfAbsentAt(filePath, resourceType.getDefaultResourcePath());
        this.resourceFileMapping.put(resourceType, filePath);
    }

    private void ensureConfigDirectoryExists() {
        configFolderPath = determineConfigFolderPath();

        try {
            Files.createDirectories(configFolderPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directories for path: " + configFolderPath, e);
        }
    }

    public String readResource(ResourceType resourceType) throws IOException {
        return Files.readString(resourceFileMapping.get(resourceType));
    }

    public void writeResource(ResourceType resourceType, String data) throws IOException {
        Files.writeString(resourceFileMapping.get(resourceType), data);
    }

    private void createFileIfAbsentAt(Path targetPath, String defaultResource) {
        if (!Files.exists(targetPath)) {
            try (InputStream inputStream = loadResourceStream(defaultResource)) {

                Files.write(targetPath, inputStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to " + targetPath, e);
            }
        }
    }

    private InputStream loadResourceStream(String resourcePath) throws FileNotFoundException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new FileNotFoundException("Unable to locate resource: " + resourcePath);
        }
        return resourceStream;
    }

    private static Path determineConfigFolderPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String userHomeDir = System.getProperty("user.home");

        if (osName.contains("win")) {
            return Paths.get(userHomeDir, "AppData", "Local", APP_NAME);
        } else if (osName.contains("nux") || osName.contains("nix")) {
            return Paths.get(userHomeDir, ".config", APP_NAME);
        } else if (osName.contains("mac")) {
            return Paths.get(userHomeDir, "Library", "Application Support", APP_NAME);
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + osName);
        }
    }
}
