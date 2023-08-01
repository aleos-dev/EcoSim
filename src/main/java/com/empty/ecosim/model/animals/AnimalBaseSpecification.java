package com.empty.ecosim.model.animals;

import com.empty.ecosim.utils.FileService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AnimalBaseSpecification {
   public record AnimalSpec(
    @JsonProperty("weight") int weight,
    @JsonProperty("speed") int speed,
    @JsonProperty("maxSatiety") int maxSatiety,
    @JsonProperty("edibleTypes") Map<Animal.Type, String> edibleTypes
) {}
    private static Map<Animal.Type, AnimalSpec> SPEC_MAP;

    static {
        FileService fs = new FileService();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonContentOfAnimalSpec = fs.read(fs.PATH_TO_ANIMALS_SPEC);

            SPEC_MAP = objectMapper.readValue(jsonContentOfAnimalSpec, HashMap.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SPEC_MAP", e);
        }

    }

    private AnimalBaseSpecification() {
        throw new UnsupportedOperationException("AnimalBaseSpecification should not be instantiated");
    }
    public static AnimalSpec getSpec(Animal.Type type) {
        return SPEC_MAP.get(type);
    }
}
