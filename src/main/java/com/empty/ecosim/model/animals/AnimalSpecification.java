package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.configuration.TypeSpecification;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record AnimalSpecification(
        @JsonProperty("weight") int weight,
        @JsonProperty("speed") int speed,
        @JsonProperty("maxSatiety") int maxSatiety,
        @JsonProperty("edibleTypes") Map<EntityType, String> edibleTypes
) implements TypeSpecification {
        public AnimalSpecification {
            if (weight < 0 || speed < 0 || maxSatiety < 0) {
                throw new IllegalArgumentException("Fields of AnimalSpec must be positive");
            }
        }
}
