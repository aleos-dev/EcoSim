package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.model.configuration.userSetup.UserSetupManager;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an abstract territory which can contain various organisms.
 */
public abstract class Territory {

    /**
     * Enum representing cardinal directions.
     */
    public enum Direction {
        NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * @return the x-coordinate offset for this direction.
         */
        public int getX() {
            return x;
        }

        /**
         * @return the y-coordinate offset for this direction.
         */
        public int getY() {
            return y;
        }
    }

    protected static final EntitySpecificationLoader<TerritoryType, IslandSpecification> TERRITORY_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ISLAND, new TypeReference<>() {
            }
    );

    /**
     * Gets a random destination cell based on a starting cell and speed.
     * @param cell the starting cell.
     * @param speed the speed or range to calculate destination.
     * @return the destination cell.
     */
    public abstract Cell getRandomDestination(Cell cell, int speed);

    /**
     * Retrieves the maximum resident count for a given organism type.
     * @param type the organism type.
     * @return the maximum resident count.
     */
    public abstract int getMaxResidentCountForOrganismType(OrganismType type);

    /**
     * @return the type of this territory.
     */
    public abstract TerritoryType getType();

    List<Cell> cells = new ArrayList<>();

    /**
     * @return a list of cells present in this territory.
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Retrieves the maximum capacity for a given organism type in the territory.
     * @param organismType the type of the organism.
     * @return the maximum capacity.
     */
    public int getMaximumCapacityFor(OrganismType organismType) {
        return TERRITORY_SPECIFICATION.getSpecificationForType(this.getType()).organismCapacity().get(organismType);
    }

    /**
     * Loads the user setup for the maximum count of organisms on a cell.
     */
    public void loadUserSetupForMaxOrganismCountOnCell() {
        for (Map.Entry<OrganismType, Integer> entry : UserSetupManager.INSTANCE.get().maxOrganismTypeCountOnCell().entrySet()) {
            setMaximumCapacityFor(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sets the maximum capacity for a given organism type in the territory.
     * @param organismType the type of the organism.
     * @param maxCapacity the maximum capacity.
     */
    private void setMaximumCapacityFor(OrganismType organismType, int maxCapacity) {
        TERRITORY_SPECIFICATION.getSpecificationForType(this.getType()).organismCapacity().put(organismType, maxCapacity);
    }
}
