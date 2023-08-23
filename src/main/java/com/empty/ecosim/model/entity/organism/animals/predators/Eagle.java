package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Eagle extends PredatorAnimal {

    private static int offspringCount = 2;
    private static int fertilePeriod = 5;
    @Override
    public int getOffspringCount() {
        return offspringCount;
    }

    @Override
    public void setOffspringCount(int offspringCount) {
        Eagle.offspringCount = offspringCount;
    }

    @Override
    public int getFertilePeriod() {
        return fertilePeriod;
    }

    @Override
    public void setFertilePeriod(int fertilePeriod) {
        Eagle.fertilePeriod = fertilePeriod;
    }

    @Override
    public AnimalType getType() {
        return AnimalType.EAGLE;
    }


}
