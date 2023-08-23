package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Fox extends PredatorAnimal {

    private static int offspringCount = 2;
    private static int fertilePeriod = 5;
    @Override
    public int getOffspringCount() {
        return offspringCount;
    }

    @Override
    public void setOffspringCount(int offspringCount) {
        Fox.offspringCount = offspringCount;
    }

    @Override
    public int getFertilePeriod() {
        return fertilePeriod;
    }

    @Override
    public void setFertilePeriod(int fertilePeriod) {
        Fox.fertilePeriod = fertilePeriod;
    }

    @Override
    public AnimalType getType() {
        return AnimalType.FOX;
    }

}
