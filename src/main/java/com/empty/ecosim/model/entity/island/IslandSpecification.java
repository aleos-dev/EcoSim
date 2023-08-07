package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.EntitySpecification;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record IslandSpecification(
        @JsonProperty("width") int width,
        @JsonProperty("height") int height,
        @JsonProperty("organismCapacity")
        Map<OrganismType, Integer> organismCapacity
) implements EntitySpecification {
    public IslandSpecification {
        if (width < 10 || height < 10) {
            throw new IllegalArgumentException("Dimensions should be at least 10x10");
        }
    }
}
