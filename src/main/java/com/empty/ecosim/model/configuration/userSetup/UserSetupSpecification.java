package com.empty.ecosim.model.configuration.userSetup;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Collections;
import java.util.Map;

/**
 * Represents the user setup specification containing configurations related to
 * the simulation environment and organism settings.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserSetupSpecification(
        @JsonProperty("width") int width,
        @JsonProperty("height") int height,
        @JsonDeserialize(keyUsing = OrganismType.OrganismTypeKeyDeserializer.class)
        @JsonProperty("startOrganismTypeCountOnCell") Map<OrganismType, Integer> startOrganismTypeCountOnCell,
        @JsonDeserialize(keyUsing = OrganismType.OrganismTypeKeyDeserializer.class)
        @JsonProperty("maxOrganismTypeCountOnCell") Map<OrganismType, Integer> maxOrganismTypeCountOnCell,
        @JsonDeserialize(keyUsing = OrganismType.OrganismTypeKeyDeserializer.class)
        @JsonProperty("animalTypeFertilityPeriod") Map<OrganismType, Integer> animalTypeFertilityPeriod,
        @JsonDeserialize(keyUsing = OrganismType.OrganismTypeKeyDeserializer.class)
        @JsonProperty("animalTypeOffspringCount") Map<OrganismType, Integer> animalTypeOffspringCount,
        double plantGrowthThreshold,
        int printStatisticInterval,
        int timeToExecuteSimulation
) {
    /**
     * Constructor for creating a {@link UserSetupSpecification} instance.
     * Initializes map fields to empty if they are null.
     *
     * @param width                        Width of the environment grid.
     * @param height                       Height of the environment grid.
     * @param startOrganismTypeCountOnCell Initial count of organisms per cell based on their type.
     * @param maxOrganismTypeCountOnCell   Maximum count of organisms per cell based on their type.
     * @param animalTypeFertilityPeriod    Fertility period setting for different animal types.
     * @param animalTypeOffspringCount     Offspring count setting for different animal types.
     * @param plantGrowthThreshold         Growth threshold for plants.
     * @param printStatisticInterval       Interval for printing out statistics.
     * @param timeToExecuteSimulation      Total time to execute the simulation.
     */
    @JsonCreator
    public UserSetupSpecification {
        if (startOrganismTypeCountOnCell == null) {
            startOrganismTypeCountOnCell = Collections.emptyMap();
        }
        if (maxOrganismTypeCountOnCell == null) {
            maxOrganismTypeCountOnCell = Collections.emptyMap();
        }
        if (animalTypeFertilityPeriod == null) {
            animalTypeFertilityPeriod = Collections.emptyMap();
        }
        if (animalTypeOffspringCount == null) {
            animalTypeOffspringCount = Collections.emptyMap();
        }
    }
}
