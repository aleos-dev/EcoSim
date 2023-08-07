package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.model.entity.EntityType;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.empty.ecosim.model.entity.island.TerritoryType.ISLAND;

public abstract class Territory {
    protected static final EntitySpecificationLoader<TerritoryType, IslandSpecification> TERRITORY_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ISLAND, new TypeReference<>(){}
    );

    List<Cell> cells = new ArrayList<>();



//    public abstract void clean();
    public abstract Cell getPossibleDestinationBasedOnSpeed(Cell cell, int speed);

    public abstract void moveResidentFromTo(Organism resident, Cell sourceCell, Cell destinationCell);

    public List<Cell> getCells() {
        return cells;
    }

    public abstract Set<OrganismType> getInhabitantTypes();
    public abstract int getMaxResidentNumber(OrganismType type);


}
