package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public abstract class Territory {
    public enum Direction {
        NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    protected static final EntitySpecificationLoader<TerritoryType, IslandSpecification> TERRITORY_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ISLAND, new TypeReference<>() {
            }
    );

    public abstract Cell getRandomDestination(Cell cell, int speed);

    public abstract int getMaxResidentCountForOrganismType(OrganismType type);

    public abstract TerritoryType getType();

    List<Cell> cells = new ArrayList<>();

    public List<Cell> getCells() {
        return cells;
    }

    public int getMaximumCapacityFor(OrganismType organismType) {
        return TERRITORY_SPECIFICATION.getSpecificationForType(this.getType()).organismCapacity().get(organismType);
    }


}
