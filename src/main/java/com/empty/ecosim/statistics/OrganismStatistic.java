package com.empty.ecosim.statistics;

import com.empty.ecosim.model.entity.organism.OrganismType;

import java.util.HashMap;
import java.util.Map;

public class OrganismStatistic implements Statistic {


    private final Map<OrganismType, Integer> newbornStatistic = new HashMap<>();

    public OrganismStatistic calculateAnimalStatistic() {
        // Calculate and return the animal statistics
        return null;
    }

    public void addNewbornStatistic(OrganismType type, int numberOfNewborns) {
        newbornStatistic.compute(type, (k, v) -> (v == null) ? numberOfNewborns : v + numberOfNewborns);
    }

    @Override
    public String toString() {
        return "OrganismStatistic{" +
                "newbornStatistic=" + newbornStatistic +
                '}';
    }
}
