package com.empty.ecosim.model.island;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.EntityType;
import com.empty.ecosim.model.animals.Animal;
import com.empty.ecosim.model.animals.AnimalType;
import com.empty.ecosim.model.plants.Plant;
import com.empty.ecosim.model.plants.PlantType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cell {
    private final Map<EntityType, Set<Entity>> entityMap = new HashMap<>();



    public Map<EntityType, Set<Entity>> getEntityMap() {
        return entityMap;
    }

    public <T extends EntityType> Set<? extends Entity>  getEntitiesFor(T type) {
        return entityMap.get(type);
    }

    public void addEntity(Entity entity) {
        entityMap.computeIfAbsent(entity.getType(), k -> new HashSet<>()).add(entity);
    }

    public boolean remove(Entity entity) {
        return entityMap.get(entity.getType()).remove(entity);
    }

    @Override

    public String toString() {
        return "Cell{" +
                "map=" + entityMap +
                '}';
    }
}
