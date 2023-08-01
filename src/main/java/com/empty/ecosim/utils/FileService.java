package com.empty.ecosim.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    public final Path PATH_TO_ANIMALS_SPEC;
    public final Path PATH_TO_CELL_SPEC;

    public FileService() {
        String appName = "Eco-Sim";
        String os = System.getProperty("os.name").toLowerCase();
        String homeDir = System.getProperty("user.home");

        Path pathToConfigFolder;
        if (os.toLowerCase().contains("win")) {
            pathToConfigFolder = Path.of(homeDir + "/AppData/Local/" + appName);
        } else if (os.contains("nux") || os.contains("nix")) {
            pathToConfigFolder = Path.of(homeDir + "/.config/" + appName);
        } else if (os.contains("mac")) {
            pathToConfigFolder = Path.of(homeDir + "/Library/Application Support/" + appName);
        } else {
            throw new UnsupportedOperationException("Unsupported operating system");
        }

        try {
            Files.createDirectories(pathToConfigFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directories for path: " + pathToConfigFolder, e);
        }

        PATH_TO_ANIMALS_SPEC = Path.of(pathToConfigFolder + "/animalsSpec.json");
        PATH_TO_CELL_SPEC = Path.of(pathToConfigFolder + "/cellSpec.json");

        if (!Files.exists(PATH_TO_ANIMALS_SPEC)) {
            try {
                String filename = "com/empty/ecosim/model/animals/animalsSpec.json";
                InputStream is = getFileFromResourceAsStream(filename);

                // Reading the InputStream content into a String
                String jsonContentOfAnimalSpec = new String(is.readAllBytes());

                Files.write(PATH_TO_ANIMALS_SPEC, jsonContentOfAnimalSpec.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public String read(Path path) throws IOException {
        return Files.readString(path);
    }

    public void write(Path path, String data) throws IOException {
        Files.writeString(path, data);
    }
}
