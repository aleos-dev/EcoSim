package com.empty.ecosim.model.entity.organism.plants;

public class Grass extends Plant {

    private static int seedCount = 3;
    private static int growthPeriod = 1;

    @Override
    public int getFertilePeriod() {
        return growthPeriod;
    }

    @Override
    public void setFertilePeriod(int growthPeriod) {
        Grass.growthPeriod = growthPeriod;
    }

    @Override
    public int getOffspringCount() {
        return seedCount;
    }

    @Override
    public void setOffspringCount(int offspringCount) {
        Grass.seedCount = offspringCount;
    }

    @Override
    public PlantType getType() {
        return PlantType.GRASS;
    }

}
