package com.empty.ecosim.model.configuration;

import com.empty.ecosim.model.configuration.ConfigurationManager.ResourceType;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalSpecification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A utility class for loading entity specifications from resource files.
 * This loader can handle various entity types and their specifications, depending on the provided configurations.
 *
 * @param <EntityTypeKey> Type parameter representing the key type for identifying entities.
 * @param <SpecType> Type parameter representing the entity specification type.
 */
public class EntitySpecificationLoader<EntityTypeKey, SpecType extends EntitySpecification> {

    private final Map<EntityTypeKey, SpecType> entitySpecification;

    /**
     * Constructor that initializes the entity specification map based on the provided resource type and type reference.
     *
     * @param resourceType The type of resource to load from.
     * @param typeRef Type reference for mapping the loaded resource to a specified type.
     */
    public EntitySpecificationLoader(ResourceType resourceType, TypeReference<Map<EntityTypeKey, SpecType>> typeRef) {
        this.entitySpecification = initializeSpecificationMap(resourceType, typeRef);
    }

    /**
     * Initializes the entity specification map based on the given resource type and type reference.
     *
     * @param resourceType The type of resource to load from.
     * @param typeRef Type reference for mapping the loaded resource to a specified type.
     * @return A map containing entity specifications.
     * @throws SpecificationInitializationException If there's an issue while initializing the specification map.
     */
    private Map<EntityTypeKey, SpecType> initializeSpecificationMap(ResourceType resourceType, TypeReference<Map<EntityTypeKey, SpecType>> typeRef) {
        try {
            ObjectMapper objectMapper = configureObjectMapper();
            String jsonContent = ConfigurationManager.INSTANCE.readResource(resourceType);
            Map<EntityTypeKey, SpecType> map = objectMapper.readValue(jsonContent, typeRef);
            map.remove(null);

            if (resourceType == ResourceType.ANIMAL) {
                map = map.entrySet().stream()
                        .filter(entry -> entry.getValue() instanceof AnimalSpecification)
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

    /**
     * Configures the ObjectMapper used to parse the JSON specifications.
     *
     * @return A configured instance of ObjectMapper.
     */
    private ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(OrganismType.class, new OrganismType.OrganismTypeKeyDeserializer());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        return objectMapper;
    }

    /**
     * Retrieves the entity specification associated with a given type.
     *
     * @param type The type for which the specification is requested.
     * @return The associated entity specification.
     * @throws SpecificationNotFoundException If the specification for the given type is not found.
     */
    public SpecType getSpecificationForType(EntityTypeKey type) {
        return Optional.ofNullable(entitySpecification.get(type)).orElseThrow(
                () -> new SpecificationNotFoundException("No specification for type: " + type)
        );
    }

    /**
     * Exception indicating that there was a problem during the initialization of the entity specification.
     */
    public static class SpecificationInitializationException extends RuntimeException {
        public SpecificationInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception indicating that the specification for a given entity type was not found.
     */
    public static class SpecificationNotFoundException extends RuntimeException {
        public SpecificationNotFoundException(String message) {
            super(message);
        }
    }
}
