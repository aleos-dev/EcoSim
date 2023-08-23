package com.empty.ecosim.model.entity.organism;

import com.empty.ecosim.model.entity.EntityType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.util.Arrays;

/**
 * Represents a type of organism in the ecosystem.
 */
public interface OrganismType extends EntityType {

    /**
     * A custom key deserializer for converting JSON keys into corresponding {@link OrganismType} values.
     * This deserializer can handle both animal and plant type keys.
     */
    class OrganismTypeKeyDeserializer extends KeyDeserializer {

        /**
         * Deserializes a given JSON key into an instance of {@link OrganismType}.
         * This method first checks if the key matches any known animal types.
         * If not, it then checks against known plant types.
         * If still unmatched, a log is printed indicating the unknown type.
         *
         * @param key The JSON key string to be deserialized.
         * @param context The deserialization context.
         * @return The corresponding {@link OrganismType} value, or null if the key doesn't match any known types.
         */
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

