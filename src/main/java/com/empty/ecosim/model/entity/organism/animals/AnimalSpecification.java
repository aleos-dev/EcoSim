package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.configuration.EntitySpecification;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Represents the specification for an animal entity in the ecosystem simulation.
 * This includes weight, speed, max satiety, and the types of organisms it can eat.
 */
public record AnimalSpecification(
        @JsonProperty("weight") double weight,
        @JsonProperty("speed") int speed,
        @JsonProperty("maxSatiety") double maxSatiety,
        @JsonProperty("edibleTypes") Map<OrganismType, Double> edibleTypes

) implements EntitySpecification {

    /**
     * Constructor that ensures weight, speed, and maxSatiety are valid.
     *
     * @throws IllegalArgumentException if any field is negative.
     */
    public AnimalSpecification {
        if (weight < 0 || speed < 0 || maxSatiety < 0) {
            throw new IllegalArgumentException("Fields of AnimalSpec must be positive");
        }
    }

    /**
     * Cleans and sorts the edible types of the animal specification.
     *
     * @return A new AnimalSpecification with cleaned and sorted edible types.
     */
    public EntitySpecification cleanAndSortEdibleTypes() {
        Map<OrganismType, Double> sortedEdibleTypes = filterAndSortEdibleTypes();
        return new AnimalSpecification(weight, speed, maxSatiety, sortedEdibleTypes);
    }

    /**
     * Filters and sorts the edible types based on their values in descending order.
     *
     * @return A map of sorted and filtered edible types.
     */
    private Map<OrganismType, Double> filterAndSortEdibleTypes() {
        return edibleTypes.entrySet().stream()
                .filter(this::isValidEntry)
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * Validates an entry to check if it's a valid edible type.
     *
     * @param entry The map entry representing an edible type.
     * @return true if the entry is valid, false otherwise.
     */
    private boolean isValidEntry(Map.Entry<OrganismType, Double> entry) {
        return entry.getKey() != null && entry.getValue() > 0;
    }

    /**
     * Gets the chance for the animal to successfully hunt a given organism type.
     *
     * @param organismType The type of organism to check the hunt success rate for.
     * @return The chance (probability) of a successful hunt, or 0 if the type is not edible.
     */
    public double getChanceToHunt(OrganismType organismType) {
        return edibleTypes.getOrDefault(organismType, 0.0);
    }
}
