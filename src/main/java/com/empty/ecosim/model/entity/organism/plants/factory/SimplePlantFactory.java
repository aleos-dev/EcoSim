package com.empty.ecosim.model.entity.organism.plants.factory;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.model.entity.organism.plants.Grass;
import com.empty.ecosim.model.entity.organism.plants.Plant;
import com.empty.ecosim.model.entity.organism.plants.PlantSpecification;
import com.empty.ecosim.model.entity.organism.plants.PlantType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.fasterxml.jackson.core.type.TypeReference;

public class SimplePlantFactory implements PlantFactory {
    private static final EntitySpecificationLoader<PlantType, PlantSpecification> PLANTS_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.PLANT, new TypeReference<>(){}
    );

    public Plant create(PlantType plantType) {
        Plant plant = switch (plantType) {
            case GRASS -> new Grass();
        };
        var spec = PLANTS_SPECIFICATION.getSpecificationForType(plantType);
        plant.setWeight(spec.weight());
        StatisticsCollector.increasePopulationCount(plantType, 1);
        StatisticsCollector.registerNewbornCount(plantType, 1);

        return plant;
    }
}

