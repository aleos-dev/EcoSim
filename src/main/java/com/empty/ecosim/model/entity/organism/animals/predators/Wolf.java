package com.empty.ecosim.model.entity.organism.animals.predators;

import com.empty.ecosim.model.entity.organism.animals.Animal;
import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.utils.RandomGenerator;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Wolf extends PredatorAnimal {
    @Override
    public Set<? extends Animal> reproduce() {

        if (gender == Gender.MALE) {
            return Collections.emptySet();
        }

        return Stream.generate(() -> {
                    Wolf child = new Wolf();

                    child.setWeight(baseSpecification.weight());
                    child.setSpeed(baseSpecification.speed());
                    child.setSatiety(baseSpecification.maxSatiety());
                    child.setBaseSpecification(baseSpecification);
                    child.setGender(RandomGenerator.generateGender());
                    child.setEdibleTypes(baseSpecification.edibleTypes());

                    return child;
                })
                .limit(RandomGenerator.getRandomInt(maxOffspring()))
                .collect(Collectors.toSet());
    }
    @Override
    public AnimalType getType() {
        return AnimalType.WOLF;
    }
}
