package com.empty.ecosim.model.configuration;

import com.empty.ecosim.model.animals.AnimalSpecification;
import com.empty.ecosim.model.configuration.ConfigurationManager.ResourceType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the specification of entities based on a given resource.
 *
 * @param <EntityTypeKey> the type of entity
 * @param <SpecType> the specification type
 */
public class EntitySpecificationLoader<EntityTypeKey, SpecType extends TypeSpecification> {

    private final Map<EntityTypeKey, SpecType> entitySpecifications;

    /**
     * Constructor for EntitySpecificationLoader.
     *
     * @param resourceType the resource type containing the specs
     * @param typeRef      the type reference for deserialization
     */
    public EntitySpecificationLoader(ResourceType resourceType, TypeReference<Map<EntityTypeKey, SpecType>> typeRef) {
        this.entitySpecifications = initializeSpecificationMap(resourceType, typeRef);
    }

    private Map<EntityTypeKey, SpecType> initializeSpecificationMap(ResourceType resourceType, TypeReference<Map<EntityTypeKey, SpecType>> typeRef) {
        try {
            ObjectMapper objectMapper = configureObjectMapper();
            String jsonContent = ConfigurationManager.INSTANCE.readResource(resourceType);
            Map<EntityTypeKey, SpecType> map = objectMapper.readValue(jsonContent, typeRef);
            map.remove(null);
            if (resourceType == ResourceType.ANIMAL) {
                map.values().forEach(x -> ((AnimalSpecification) x).edibleTypes().remove(null));
            }
            return map;

        } catch (IOException e) {
            throw new SpecificationInitializationException("Failed to initialize specification map", e);
        }
    }

    private ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(com.empty.ecosim.model.EntityType.class, new com.empty.ecosim.model.EntityType.EntityTypeKeyDeserializer());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        return objectMapper;
    }

    /**
     * Fetches the specification for the given type.
     *
     * @param type the entity type
     * @return the corresponding specification
     */
    public SpecType getSpecificationForType(EntityTypeKey type) {
        return Optional.ofNullable(entitySpecifications.get(type)).orElseThrow(
                () -> new SpecificationNotFoundException("No specification for type: " + type)
        );
    }

    // Add domain-specific exceptions for better clarity and error handling
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
