package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Territory {
    protected static final EntitySpecificationLoader<TerritoryType, IslandSpecification> TERRITORY_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ISLAND, new TypeReference<>(){}
    );

    List<Cell> cells = new ArrayList<>();


    public int getMaximumCapacityFor(OrganismType organismType) {
        return TERRITORY_SPECIFICATION.getSpecificationForType(this.getType()).organismCapacity().get(organismType);
    }
    public abstract Cell getRandomAdjacentCell(Cell cell, int speed);

//    public abstract void travelFromTo(Organism resident, Cell sourceCell, Cell destinationCell);

    public List<Cell> getCells() {
        return cells;
    }

    public abstract Set<OrganismType> getResidentsTypes();
    public abstract int getMaxResidentCountForOrganismType(OrganismType type);

    public abstract TerritoryType getType();

}
