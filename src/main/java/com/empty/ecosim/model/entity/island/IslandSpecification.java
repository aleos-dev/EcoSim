package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.EntitySpecification;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Represents the specification for an Island, including its dimensions and the capacity
 * for different organism types.
 */
public record IslandSpecification(
        @JsonProperty("width") int width,
        @JsonProperty("height") int height,
        @JsonProperty("organismCapacity")
        Map<OrganismType, Integer> organismCapacity
) implements EntitySpecification {

}
