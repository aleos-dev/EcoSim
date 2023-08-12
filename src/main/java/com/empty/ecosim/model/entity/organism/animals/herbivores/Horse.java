package com.empty.ecosim.model.entity.organism.animals.herbivores;

import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Horse extends HerbivoreAnimal {
    @Override
    public Set<? extends Animal> reproduce() {

        if (gender == Gender.MALE || RandomGenerator.getRandomInt(getFertilePeriod()) > 0) {
            return Collections.emptySet();
        }

        return Stream.generate(() -> {
                    Horse child = new Horse();
                    return copyGenesTo(child);
                })
                .limit(RandomGenerator.getRandomInt(maxOffspring()))
                .collect(Collectors.toSet());
    }

    @Override
    public AnimalType getType() {
        return AnimalType.HORSE;
    }
}
