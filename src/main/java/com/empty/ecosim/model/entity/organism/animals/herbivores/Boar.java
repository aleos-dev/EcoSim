package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;

public class Boar extends HerbivoreAnimal {

    private static int offspringCount = 2;
    private static int fertilePeriod = 5;

    @Override
    public int getOffspringCount() {
        return offspringCount;
    }

    @Override
    public void setOffspringCount(int offspringCount) {
        Boar.offspringCount = offspringCount;
    }

    @Override
    public int getFertilePeriod() {
        return fertilePeriod;
    }

    @Override
    public void setFertilePeriod(int fertilePeriod) {
        Boar.fertilePeriod = fertilePeriod;
    }

    @Override
    public AnimalType getType() {
        return AnimalType.BOAR;
    }
}
