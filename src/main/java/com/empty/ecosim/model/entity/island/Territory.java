package com.empty.ecosim.model.entity.island;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.empty.ecosim.model.configuration.EntitySpecificationLoader;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Territory {
    protected static final EntitySpecificationLoader<TerritoryType, IslandSpecification> TERRITORY_SPECIFICATION = new EntitySpecificationLoader<>(
            ConfigurationManager.ResourceType.ISLAND, new TypeReference<>(){}
    );

    List<Cell> cells = new ArrayList<>();


    public void main(String[] args) {
        cells.stream().forEach(c -> c.getResidentCountByType(AnimalType.DEER));
    }
    public abstract Cell getRandomPossibleDestination(Cell cell, int speed);

    public abstract void moveOrganismFromSourceToDestination(Organism resident, Cell sourceCell, Cell destinationCell);

    public List<Cell> getCells() {
        return cells;
    }

    public abstract Set<OrganismType> getResidentOrganismTypes();
    public abstract int getMaxResidentCountForOrganismType(OrganismType type);


}
