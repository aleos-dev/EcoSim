package com.empty.ecosim.model.entity.organism.plants.factory;

import com.empty.ecosim.model.entity.organism.plants.Plant;
import com.empty.ecosim.model.entity.organism.plants.PlantType;

public  interface PlantFactory {
    Plant create(PlantType type);
}
