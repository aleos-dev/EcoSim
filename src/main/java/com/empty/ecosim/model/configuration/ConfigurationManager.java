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
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();
    private static final String APP_NAME = "EcosystemSimulator(aleos-dev)";
    private static Path configFolderPath;
    private final Map<ResourceType, Path> resourceFileMapping = new HashMap<>();

    /**
     * Private constructor for the singleton ConfigurationManager class.
     * Initializes the configuration directory and sets up resource paths.
     */
    private ConfigurationManager() {
        ensureConfigDirectoryExists();

        Arrays.stream(ResourceType.values()).forEach(this::setupResourcePath);
    }

    /**
     * Sets up the appropriate file path for a given resource type.
     *
     * @param resourceType the type of resource whose path is to be set up.
     */
    private void setupResourcePath(ResourceType resourceType) {

        Path filePath = Paths.get(configFolderPath.toString(), resourceType.getConfigFileName());
        createFileIfAbsentAt(filePath, resourceType.getDefaultResourcePath());
        this.resourceFileMapping.put(resourceType, filePath);
    }

    /**
     * Ensures that the configuration directory exists.
     * If it doesn't, the directory is created.
     */
    private void ensureConfigDirectoryExists() {
        configFolderPath = OSConfigPathStrategy.getConfigPathForCurrentOS();

        try {
            Files.createDirectories(configFolderPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directories for path: " + configFolderPath, e);
        }
    }

    /**
     * Reads the resource contents from the file mapped to the provided resource type.
     *
     * @param resourceType the type of resource to be read.
     * @return a string representation of the resource contents.
     * @throws IOException if there's an error reading the file.
     */
    public String readResource(ResourceType resourceType) throws IOException {
        return Files.readString(resourceFileMapping.get(resourceType));
    }


    /**
     * Creates a file at the specified target path with content from a default resource,
     * if the file does not already exist.
     *
     * @param targetPath the path where the file should be created.
     * @param defaultResource the default resource path to retrieve content from.
     */
    private void createFileIfAbsentAt(Path targetPath, String defaultResource) {
        if (!Files.exists(targetPath)) {
            try (InputStream inputStream = loadResourceStream(defaultResource)) {

                Files.write(targetPath, inputStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to " + targetPath, e);
            }
        }
    }

    /**
     * Loads an InputStream for the resource at the provided path.
     *
     * @param resourcePath the path to the resource.
     * @return an InputStream for the resource.
     * @throws FileNotFoundException if the resource cannot be located.
     */
    private InputStream loadResourceStream(String resourcePath) throws FileNotFoundException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new FileNotFoundException("Unable to locate resource: " + resourcePath);
        }
        return resourceStream;
    }

    /**
     * Gets a mapping of resource types to their file paths.
     *
     * @return a map with resource types as keys and their corresponding paths as values.
     */
    public Map<ResourceType, Path> getResourceFileMapping() {
        return resourceFileMapping;
    }

    /**
     * Enumeration representing various operating system strategies to determine the configuration path.
     * This is used to decide where configuration and other files related to the application will be stored
     * based on the operating system the application is running on.
     */
    public enum OSConfigPathStrategy {
        WINDOWS("win", "AppData", "Local"),
        LINUX("nux", ".config"),
        UNIX("nix", ".config"),
        MAC("mac", "Library", "Application Support");

        private final String osIdentifier;
        private final String[] pathComponents;

        /**
         * Constructor for creating a specific OSConfigPathStrategy.
         *
         * @param osIdentifier     a string identifier related to the operating system.
         * @param pathComponents   the path components used to build the path to the configuration directory.
         */
        OSConfigPathStrategy(String osIdentifier, String... pathComponents) {
            this.osIdentifier = osIdentifier;
            this.pathComponents = pathComponents;
        }

        /**
         * Determines and returns the appropriate configuration path for the current operating system.
         *
         * @return the path where the application's configuration files should be located.
         * @throws UnsupportedOperationException if the operating system is not supported.
         */
        public static Path getConfigPathForCurrentOS() {
            String osName = System.getProperty("os.name").toLowerCase();
            String userHomeDir = System.getProperty("user.home");

            for (OSConfigPathStrategy strategy : values()) {
                if (osName.contains(strategy.osIdentifier)) {
                    return Paths.get(userHomeDir, strategy.pathComponents).resolve(APP_NAME);
                }
            }

            throw new UnsupportedOperationException("Unsupported operating system: " + osName);
        }
    }

    /**
     * Enumeration representing various types of resources required by the application.
     * Each type corresponds to a specific file containing configurations or definitions related to the application's operation.
     */
    public enum ResourceType {
        ANIMAL("animalsSpec.json", "com/empty/ecosim/model/entity/organism/animals/animalsSpec.json"),
        PLANT("plantsSpec.json", "com/empty/ecosim/model/entity/organism/plants/plantsSpec.json"),
        ISLAND("islandSpec.json", "com/empty/ecosim/model/entity/island/islandSpec.json"),
        USER_SETUP("config.yaml", "com/empty/ecosim/model/configuration/config.yaml");

        private final String configFileName;
        private final String defaultResourcePath;

        /**
         * Constructor for creating a specific ResourceType.
         *
         * @param configFileName       the name of the file where this resource's data is stored.
         * @param defaultResourcePath  the default path, usually within the JAR, where the resource can be found.
         */
        ResourceType(String configFileName, String defaultResourcePath) {
            this.configFileName = configFileName;
            this.defaultResourcePath = defaultResourcePath;
        }

        /**
         * Gets the configuration file name for this resource type.
         *
         * @return the file name.
         */
        public String getConfigFileName() {
            return configFileName;
        }

        /**
         * Gets the default path where this resource can be found, usually within the application JAR.
         *
         * @return the default resource path.
         */
        public String getDefaultResourcePath() {
            return defaultResourcePath;
        }
    }
}
