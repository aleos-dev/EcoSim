package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.EntitySpecification;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Represents the specification for an Island, including its dimensions and the capacity
 * for different organism types. This record ensures that the island has valid dimensions.
 */
public record IslandSpecification(
        @JsonProperty("width") int width,
        @JsonProperty("height") int height,
        @JsonProperty("organismCapacity")
        Map<OrganismType, Integer> organismCapacity
) implements EntitySpecification {
    public IslandSpecification {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Dimensions should be at least 10x10");
        }
    }
}
