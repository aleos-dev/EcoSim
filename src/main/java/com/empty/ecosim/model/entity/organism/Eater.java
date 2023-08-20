package com.empty.ecosim.model.entity.organism;

import java.util.List;

public interface Eater {
    void eat(Organism food);

    boolean isEdible(OrganismType food);

    boolean canCaptureFood(OrganismType targetType);

    List<OrganismType> getEdibleTypes();

}
