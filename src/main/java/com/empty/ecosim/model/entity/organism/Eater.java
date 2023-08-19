package com.empty.ecosim.model.entity.organism;

public interface Eater {
    void eat(Organism food);

    boolean isEdible(OrganismType food);

    boolean isFindFoodSucceeded(OrganismType targetType);

}
