package com.empty.ecosim.model.plants;

import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.Specification;
import com.fasterxml.jackson.core.type.TypeReference;

public abstract class Plant implements EntityType {

    protected static final Specification<PlantType, PlantSpecification> PLANT_SPECIFICATION = new Specification<> (
            ConfigurationManager.ResourceType.PLANT, new TypeReference<>(){}
    );
    protected int weight;

    public abstract void reproduce();
}
