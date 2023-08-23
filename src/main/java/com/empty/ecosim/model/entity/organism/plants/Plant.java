package com.empty.ecosim.model.entity.organism.plants;

import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Plant extends Organism {

    /**
     * Allows the plant to reproduce based on certain conditions.
     *
     * @return A set of offspring plants or an empty set if reproduction doesn't occur.
     */
    @Override
    public Set<? extends Plant> reproduce() {
        if (!canReproduce()) {
            return Collections.emptySet();
        }

        return Stream.generate(this::generateChild)
                .limit(getOffspringCount())
                .collect(Collectors.toSet());
    }

    /**
     * Determines if the plant can reproduce based on its fertility.
     *
     * @return true if the plant can reproduce, false otherwise.
     */
    private boolean canReproduce() {
        return !(RandomGenerator.nextInt(getFertilePeriod()) > 0);
    }

    /**
     * Generates a child instance for the plant.
     *
     * @return A new instance of the plant.
     */
    private Plant generateChild() {
        return createChildInstance().map(this::transferGeneticTraitsTo)
                .orElseThrow(() -> new RuntimeException("Failed to create a child instance."));
    }

    /**
     * Creates a new child instance of the current plant type.
     *
     * @return An optional containing the new plant instance or empty if instantiation fails.
     */
    private Optional<Plant> createChildInstance() {
        try {
            return Optional.of(this.getClass().getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Transfers genetic traits from the parent to the child plant.
     *
     * @param child The child plant to transfer traits to.
     * @return The child plant with updated traits.
     */
    private <T extends Plant> T transferGeneticTraitsTo(T child) {
        child.setWeight(weight);
        return child;
    }

    @Override
    public abstract PlantType getType();

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
