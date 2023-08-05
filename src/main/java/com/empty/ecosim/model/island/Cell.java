package com.empty.ecosim.model.island;

import com.empty.ecosim.model.Entity;
import com.empty.ecosim.model.EntityType;

import java.util.*;

public class Cell {
    private final Map<EntityType, Set<Entity>> entityMap = new HashMap<>();

    public Map<EntityType, Set<Entity>> getEntityMap() {
        return entityMap;
    }

    public <T extends EntityType> Set<Entity>  getEntitiesFor(T type) {
        return entityMap.get(type);
    }

    public void addEntity(Entity entity) {
        entityMap.computeIfAbsent(entity.getType(), k -> new HashSet<>()).add(entity);
    }

    public boolean remove(Entity entity) {
        Set<Entity> entities = entityMap.get(entity.getType());
        if (entities == null) {
            return false;
        }
        entities.remove(entity);
        if (entities.size() == 0) {
            entityMap.remove(entity.getType());
        }
        return true;
    }

    public boolean isTypePresent(EntityType entityType) {
        return entityMap.containsKey(entityType);
    }

    @Override

    public String toString() {
        return "Cell{" +
                "map=" + entityMap +
                '}';
    }
}
