package com.empty.ecosim.model.plants.factory;

import com.empty.ecosim.model.plants.Plant;
import com.empty.ecosim.model.plants.PlantType;

public abstract class PlantFactory {
    public abstract Plant createPlant(PlantType type);
}
