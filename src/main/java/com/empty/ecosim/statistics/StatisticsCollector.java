package com.empty.ecosim.statistics;

import java.util.List;

public class StatisticsCollector {
    private List<IslandStatistic> islandStatistics;
    private List<AnimalStatistic> animalStatistics;

    public void collectIslandStatistic(IslandStatistic statistic) {
        islandStatistics.add(statistic);
    }

    public void collectAnimalStatistic(AnimalStatistic statistic) {
        animalStatistics.add(statistic);
    }
}
