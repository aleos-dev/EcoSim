package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.configuration.EntitySpecification;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public record AnimalSpecification(
        @JsonProperty("weight") double weight,
        @JsonProperty("speed") int speed,
        @JsonProperty("maxSatiety") double maxSatiety,
        @JsonProperty("edibleTypes") Map<OrganismType, Double> edibleTypes
) implements EntitySpecification {
    public AnimalSpecification {
        if (weight < 0 || speed < 0 || maxSatiety < 0) {
            throw new IllegalArgumentException("Fields of AnimalSpec must be positive");
        }
    }
    public AnimalSpecification cleanAndSortEdibleTypes() {
        var sortedEdibleTypes = edibleTypes.entrySet().stream()
                .filter(e -> e.getKey() != null)
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        sortedEdibleTypes.entrySet().removeIf(e -> e == null || e.getValue() <= 0);

        return new AnimalSpecification(weight, speed, maxSatiety, sortedEdibleTypes);
    }

    public double getChanceToHunt(OrganismType organismType) {
        return edibleTypes.get(organismType);
    }
}
