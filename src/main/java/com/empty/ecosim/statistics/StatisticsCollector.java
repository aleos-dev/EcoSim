package com.empty.ecosim.statistics;

import java.util.List;

public class StatisticsCollector {
    private List<IslandStatistic> islandStatistics;
    private List<OrganismStatistic> organismStatistics;

    public void collectIslandStatistic(IslandStatistic statistic) {
        islandStatistics.add(statistic);
    }

    public void collectAnimalStatistic(OrganismStatistic statistic) {
        organismStatistics.add(statistic);
    }
}
