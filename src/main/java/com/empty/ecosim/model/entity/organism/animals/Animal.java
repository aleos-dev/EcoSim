package com.empty.ecosim.model.entity.organism.animals;

import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.Eater;
import com.empty.ecosim.model.entity.organism.Movable;
import com.empty.ecosim.model.entity.organism.Organism;
import com.empty.ecosim.model.entity.organism.OrganismType;
import com.empty.ecosim.statistics.StatisticsCollector;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.empty.ecosim.utils.RandomGenerator.getRandomOrganismType;

/**
 * Represents a generic animal entity in the ecosystem simulation.
 * Each animal can move, eat, and reproduce.
 */
public abstract class Animal extends Organism implements Movable, Eater {

    // Inner enums and constants
    public enum Gender {MALE, FEMALE}

    private static final double DEPLETE_SATIETY_MODIFICATION = 0.15;

    // Member variables
    private int speed;
    private double satiety;
    private Gender gender;
    private AnimalSpecification baseSpecification;
    private List<OrganismType> edibleTypes;

    @Override
    public void move(Cell cell) {
        Cell destination;
        do {
            destination = cell.chooseRandomAdjasentCell(speed);
        } while (!destination.tryToLock());

        if (canTravelTo(cell, destination)) {
            cell.removeOrganism(this);
            destination.addOrganism(this);
        }
        destination.unlock();
    }

    @Override
    public void eat(Cell cell) {
        depleteSatiety();
        if (isDead()) return;

        Optional.ofNullable(findFood(cell)).ifPresent(this::consumeFood);
    }

    @Override
    public Set<Animal> reproduce() {
        if (!canReproduce()) {
            return Collections.emptySet();
        }

        return Stream.generate(this::generateChild)
                .limit(getOffspringCount())
                .collect(Collectors.toSet());
    }

    /**
     * Checks if the animal can reproduce.
     *
     * @return true if the animal can reproduce, otherwise false.
     */
    private boolean canReproduce() {
        return !(gender == Gender.MALE || RandomGenerator.nextInt(getFertilePeriod()) > 0);
    }

    /**
     * Generates a new child instance inheriting the traits of the parent.
     *
     * @return A new instance of Animal, which is the child.
     */
    private Animal generateChild() {
        return createChildInstance().map(this::transferGeneticTraitsTo)
                .orElseThrow(() -> new RuntimeException("Failed to create a child instance."));
    }

    /**
     * Creates a new child instance for the current animal type.
     *
     * @return An optional holding the new child instance, or empty if failed.
     */
    private Optional<Animal> createChildInstance() {
        try {
            return Optional.of(this.getClass().getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Transfers genetic traits from the parent animal to the child.
     *
     * @param child The child instance to which traits should be transferred.
     * @param <T> The specific animal type.
     * @return Child instance with inherited traits.
     */
    private <T extends Animal> T transferGeneticTraitsTo(T child) {
        child.setWeight(baseSpecification.weight());
        child.setSpeed(baseSpecification.speed());
        child.setSatiety(baseSpecification.maxSatiety() / 2);
        child.setBaseSpecification(baseSpecification);
        child.setGender(RandomGenerator.generateGender());
        child.setEdibleTypes(baseSpecification.edibleTypes());

        return child;
    }

    /**
     * Determines if the animal can travel from one cell to another.
     *
     * @param from The starting cell.
     * @param destination The target cell.
     * @return true if the animal can move to the destination, otherwise false.
     */
    private boolean canTravelTo(Cell from, Cell destination) {
        return from != destination && destination.canAccommodate(this.getType());
    }


   /**
     * Finds the target food organism within the given cell.
     *
     * @param cell The cell to search for food.
     * @return The target food organism, or null if not found.
     */
    private Organism findFood(Cell cell) {
        Optional<Organism> food = chooseTargetFood(cell);

        if (food.isPresent() && canCaptureFood(food.get().getType())) {
            cell.removeOrganism(food.get());
            return food.get();
        }

        return null;
    }

    /**
     * Chooses a target food type from the list of available edible types in a given cell.
     *
     * @param cell The cell to search for edible types.
     * @return An optional holding the chosen organism type, or empty if none are available.
     */
    private Optional<Organism> chooseTargetFood(Cell cell) {
        List<OrganismType> availableEdibleTypes = filterEdibleTypesInCell(cell);
        if (availableEdibleTypes.isEmpty()) {
            return Optional.empty();
        }

        var targetType = getRandomOrganismType(availableEdibleTypes);

        return cell.getOrganism(targetType);
    }

    /**
     * Consumes the food organism and updates the satiety of the animal accordingly.
     *
     * @param food The organism to be consumed as food.
     */
    protected void consumeFood(Organism food) {
        food.die();
        StatisticsCollector.registerPredationCount(food.getType());
        setSatiety(Math.min(getSatiety() + food.getWeight(), getBaseSpecification().maxSatiety()));
    }

    /**
     * Filters the list of edible organism types present in a given cell.
     *
     * @param cell The cell to filter edible types from.
     * @return A list of organism types that are edible and present in the cell.
     */
    private List<OrganismType> filterEdibleTypesInCell(Cell cell) {
        return cell.currentOrganismTypes().stream().filter(this::isEdible).collect(Collectors.toList());
    }

    /**
     * Decreases the satiety of the animal over time or due to certain actions.
     */
    protected void depleteSatiety() {
        satiety -= baseSpecification.maxSatiety() * DEPLETE_SATIETY_MODIFICATION;
        if (satiety <= 0.000001) {
            die();
            StatisticsCollector.registerStarvingCount(this.getType());
        }
    }

    @Override
    public boolean isEdible(OrganismType type) {
        return getEdibleTypes().contains(type);
    }

    public boolean canCaptureFood(OrganismType targetType) {
        return RandomGenerator.didHuntSuccesses(getBaseSpecification().getChanceToHunt(targetType));
    }

    public AnimalSpecification getBaseSpecification() {
        return baseSpecification;
    }


    public void setBaseSpecification(AnimalSpecification baseSpecification) {
        this.baseSpecification = baseSpecification;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getSatiety() {
        return satiety;
    }

    public void setSatiety(double satiety) {
        this.satiety = satiety;
    }

    @Override
    public List<OrganismType> getEdibleTypes() {
        return edibleTypes;
    }

    public void setEdibleTypes(Map<OrganismType, Double> edibleTypes) {
        this.edibleTypes = new ArrayList<>(edibleTypes.keySet());
    }
}
