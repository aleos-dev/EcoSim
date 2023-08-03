package com.empty.ecosim.model;

import com.empty.ecosim.model.animals.AnimalType;
import com.empty.ecosim.model.plants.PlantType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.util.Arrays;

public interface EntityType {

    class EntityTypeKeyDeserializer extends KeyDeserializer {
        @Override
        public EntityType deserializeKey(String key, DeserializationContext context) {
            if (Arrays.stream(AnimalType.values())
                    .anyMatch(e -> e.name().equals(key))) {
                return AnimalType.valueOf(key);
            } else if (Arrays.stream(PlantType.values())
                    .anyMatch(e -> e.name().equals(key))) {
                return PlantType.valueOf(key);
            }
            return null;
        }
    }
}

