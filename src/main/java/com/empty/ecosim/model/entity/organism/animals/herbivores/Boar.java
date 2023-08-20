package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Boar extends HerbivoreAnimal {

    @Override
    public Set<? extends Animal> reproduce() {

        if (getGender() == Gender.MALE || RandomGenerator.getInt(getFertilePeriod()) > 0) {
            return Collections.emptySet();
        }

        return Stream.generate(() -> {
                    Boar child = new Boar();
                    return transferGeneticTraitsTo(child);
                })
                .limit(RandomGenerator.getInt(getOffspringsNumber()) + 1)
                .collect(Collectors.toSet());
    }

    @Override
    public AnimalType getType() {
        return AnimalType.BOAR;
    }
}
