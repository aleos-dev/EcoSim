package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.model.configuration.OrganismSpecification;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlantSpecification(
        @JsonProperty("weight") int weight
) implements OrganismSpecification {
        public PlantSpecification {
            if (weight < 0) {
                throw new IllegalArgumentException("Weight of PlantSpec must be positive");
            }
    }
}
