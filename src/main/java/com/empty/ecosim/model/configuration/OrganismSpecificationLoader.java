package com.empty.ecosim.model.configuration;

import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalSpecification;
import com.empty.ecosim.model.configuration.ConfigurationManager.ResourceType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrganismSpecificationLoader<OrganismTypeKey, SpecType extends OrganismSpecification> {

    private final Map<OrganismTypeKey, SpecType> organismSpecifications;
    public OrganismSpecificationLoader(ResourceType resourceType, TypeReference<Map<OrganismTypeKey, SpecType>> typeRef) {
        this.organismSpecifications = initializeSpecificationMap(resourceType, typeRef);
    }

    private Map<OrganismTypeKey, SpecType> initializeSpecificationMap(ResourceType resourceType, TypeReference<Map<OrganismTypeKey, SpecType>> typeRef) {
        try {
            ObjectMapper objectMapper = configureObjectMapper();
            String jsonContent = ConfigurationManager.INSTANCE.readResource(resourceType);
            Map<OrganismTypeKey, SpecType> map = objectMapper.readValue(jsonContent, typeRef);
            map.remove(null);

            if (resourceType == ResourceType.ANIMAL) {
                map = map.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> (SpecType) ((AnimalSpecification) entry.getValue()).cleanAndSortEdibleTypes(),
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new)
                        );
            }
            return map;

        } catch (IOException e) {
            throw new SpecificationInitializationException("Failed to initialize specification map", e);
        }
    }

    private ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(OrganismType.class, new OrganismType.OrganismTypeKeyDeserializer());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        return objectMapper;
    }

    public SpecType getSpecificationForType(OrganismTypeKey type) {
        return Optional.ofNullable(organismSpecifications.get(type)).orElseThrow(
                () -> new SpecificationNotFoundException("No specification for type: " + type)
        );
    }

    public static class SpecificationInitializationException extends RuntimeException {
        public SpecificationInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class SpecificationNotFoundException extends RuntimeException {
        public SpecificationNotFoundException(String message) {
            super(message);
        }
    }
}
