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


    // Abstract methods related to type and reproduction
    public abstract AnimalType getType();

    public abstract int getFertilePeriod();

    public abstract int getOffspringsNumber();

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
                .limit(RandomGenerator.getIntRange(1, getOffspringsNumber()))
                .collect(Collectors.toSet());
    }

    // Reproduction methods
    private boolean canReproduce() {
        return !(gender == Gender.MALE || RandomGenerator.getInt(getFertilePeriod()) > 0);
    }

    private Animal generateChild() {
        return createChildInstance().map(this::transferGeneticTraitsTo)
                .orElseThrow(() -> new RuntimeException("Failed to create a child instance."));
    }

    private Optional<Animal> createChildInstance() {
        try {
            return Optional.of(this.getClass().getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private <T extends Animal> T transferGeneticTraitsTo(T child) {
        child.setWeight(baseSpecification.weight());
        child.setSpeed(baseSpecification.speed());
        child.setSatiety(baseSpecification.maxSatiety() / 2);
        child.setBaseSpecification(baseSpecification);
        child.setGender(RandomGenerator.generateGender());
        child.setEdibleTypes(baseSpecification.edibleTypes());

        return child;
    }

    // Movement methods
    private boolean canTravelTo(Cell from, Cell destination) {
        return from != destination && destination.canAccommodate(this.getType());
    }


    // Eating methods

    private Organism findFood(Cell cell) {
        Optional<Organism> food = chooseTargetFood(cell);

        if (food.isPresent() && canCaptureFood(food.get().getType())) {
            cell.removeOrganism(food.get());
            return food.get();
        }

        return null;
    }

    private Optional<Organism> chooseTargetFood(Cell cell) {
        List<OrganismType> availableEdibleTypes = filterEdibleTypesInCell(cell);
        if (availableEdibleTypes.isEmpty()) {
            return Optional.empty();
        }

        var targetType = getRandomOrganismType(availableEdibleTypes);

        return cell.getOrganism(targetType);
    }

    protected void consumeFood(Organism food) {
        food.die();
        StatisticsCollector.registerPredationCount(food.getType());
        setSatiety(Math.min(getSatiety() + food.getWeight(), getBaseSpecification().maxSatiety()));
    }

    private List<OrganismType> filterEdibleTypesInCell(Cell cell) {
        return cell.currentOrganismTypes().stream().filter(this::isEdible).collect(Collectors.toList());
    }

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
