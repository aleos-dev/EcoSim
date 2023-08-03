package com.empty.ecosim.model.configuration;

import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.configuration.ConfigurationManager.ResourceType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class Specification<T, S extends TypeSpecification> {

    private final Map<T, S> SPEC_MAP;

    public Specification(ResourceType resourceType, TypeReference<Map<T, S>> typeRef) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addKeyDeserializer(EntityType.class, new EntityType.EntityTypeKeyDeserializer());
            objectMapper.registerModule(module);
            objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

            String jsonContent = ConfigurationManager.INSTANCE.getResource(resourceType);
            SPEC_MAP = objectMapper.readValue(jsonContent, typeRef);
            SPEC_MAP.remove(null);

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize specification map", e);
        }
    }

    public S getSpec(T type) {
        return Optional.ofNullable(SPEC_MAP.get(type)).orElseThrow(
                () -> new IllegalArgumentException("No specification for type: " + type));
    }
}
