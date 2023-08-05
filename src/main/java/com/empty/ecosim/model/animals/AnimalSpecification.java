package com.empty.ecosim.model.animals;

import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.configuration.TypeSpecification;
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
        @JsonProperty("edibleTypes") Map<EntityType, Double> edibleTypes
) implements TypeSpecification {
        public AnimalSpecification {
            if (weight < 0 || speed < 0 || maxSatiety < 0) {
                throw new IllegalArgumentException("Fields of AnimalSpec must be positive");
            }
        }

    public AnimalSpecification sortSpecificationByValueDescendingOrder() {
        var sortedEdibleTypes = edibleTypes.entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return new AnimalSpecification(weight, speed, maxSatiety, sortedEdibleTypes);
    }
}