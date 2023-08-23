package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Bear extends PredatorAnimal {

    private static int offspringCount = 2;
    private static int fertilePeriod = 5;

    public int getOffspringCount() {
        return offspringCount;
    }

    public void setOffspringCount(int offspringCount) {
        Bear.offspringCount = offspringCount;
    }

    @Override
    public int getFertilePeriod() {
        return fertilePeriod;
    }

    public void setFertilePeriod(int fertilePeriod) {
        Bear.fertilePeriod = fertilePeriod;
    }

    @Override
    public AnimalType getType() {
        return AnimalType.BEAR;
    }

}