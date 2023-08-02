package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.plants.Plant;
import com.empty.ecosim.utils.ConfigurationManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class EntityBaseSpecification {
    public interface Specification {
    }

    public record AnimalSpec(
            @JsonProperty("weight") int weight,
            @JsonProperty("speed") int speed,
            @JsonProperty("maxSatiety") int maxSatiety,
            @JsonProperty("edibleTypes") Map<Animal.Type, String> edibleTypes
    ) implements Specification {
    }

    public record PlantSpec(
            @JsonProperty("weight") int weight
    ) implements Specification {
    }

    private static final Map<Animal.Type, AnimalSpec> ANIMAL_SPEC_MAP;
    private static final Map<Plant.Type, PlantSpec> PLANT_SPEC_MAP;

    static {
        var confManager = ConfigurationManager.INSTANCE;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

            String jsonContentOfAnimalsSpec = confManager.getResource(confManager.pathToAnimalsSpecificationResource);
            ANIMAL_SPEC_MAP = objectMapper.readValue(jsonContentOfAnimalsSpec, new TypeReference<>() {
            });
            // remove specs that can't be identified (not present in Animal.Type)
            ANIMAL_SPEC_MAP.forEach((key, value) -> ANIMAL_SPEC_MAP.get(key).edibleTypes.remove(null));
            ANIMAL_SPEC_MAP.remove(null);

            String jsonContentOfPlantsSpec = confManager.getResource(confManager.pathToPlantsSpecificationResource);
            PLANT_SPEC_MAP = objectMapper.readValue(jsonContentOfPlantsSpec, new TypeReference<>() {
            });

            // remove specs that can't be identified (not present in Plant.Type)
            PLANT_SPEC_MAP.remove(null);

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SPEC_MAPS", e);
        }
    }


    public static AnimalSpec getSpec(Animal.Type type) throws IllegalArgumentException {
        return Optional.ofNullable(ANIMAL_SPEC_MAP.get(type))
                .orElseThrow(() -> new IllegalArgumentException("No specification for animal type: " + type));
    }

    public static PlantSpec getSpec(Plant.Type type) throws IllegalArgumentException {
        return Optional.ofNullable(PLANT_SPEC_MAP.get(type))
                .orElseThrow(() -> new IllegalArgumentException("No specification for plant type: " + type));
    }

    private EntityBaseSpecification() {
        throw new UnsupportedOperationException("EntityBaseSpecification should not be instantiated");
    }
}
