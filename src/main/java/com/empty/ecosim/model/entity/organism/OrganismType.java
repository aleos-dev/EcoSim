package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.EntityType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.util.Arrays;

public interface OrganismType extends EntityType {

    class OrganismTypeKeyDeserializer extends KeyDeserializer {
        @Override
        public OrganismType deserializeKey(String key, DeserializationContext context) {
            if (Arrays.stream(AnimalType.values())
                    .anyMatch(e -> e.name().equals(key))) {
                return AnimalType.valueOf(key);
            } else if (Arrays.stream(PlantType.values())
                    .anyMatch(e -> e.name().equals(key))) {
                return PlantType.valueOf(key);
            }
            System.out.println("Log: unknown entity type " + key + " will be ignored.");
            return null;
        }
    }
}

