package com.empty.ecosim.model.plants;

import com.empty.ecosim.model.configuration.TypeSpecification;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlantSpecification(
        @JsonProperty("weight") int weight
) implements TypeSpecification {
        public PlantSpecification {
            if (weight < 0) {
                throw new IllegalArgumentException("Weight of PlantSpec must be positive");
            }
    }
}
