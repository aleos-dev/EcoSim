package com.empty.ecosim.model;

import com.empty.ecosim.model.animals.AnimalType;
import com.empty.ecosim.model.animals.factory.AnimalFactory;
import com.empty.ecosim.model.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.animals.herbivores.Horse;
import com.empty.ecosim.model.animals.predators.Wolf;
import com.empty.ecosim.model.island.Cell;
import com.empty.ecosim.model.plants.Grass;
import com.empty.ecosim.model.plants.factory.PlantFactory;
import com.empty.ecosim.model.plants.factory.SimplePlantFactory;

import java.util.HashSet;

import static com.empty.ecosim.model.animals.AnimalType.HORSE;
import static com.empty.ecosim.model.animals.AnimalType.WOLF;
import static com.empty.ecosim.model.plants.PlantType.GRASS;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {

        AnimalFactory animalFactory = new SimpleAnimalFactory();
        PlantFactory plantFactory = new SimplePlantFactory();

        Wolf wolf = (Wolf) animalFactory.createAnimal(WOLF);
        Wolf wolf1 = (Wolf) animalFactory.createAnimal(WOLF);
        Horse horse = (Horse) animalFactory.createAnimal(HORSE);
        Grass grass = (Grass) plantFactory.createPlant(GRASS);


        Cell cell = new Cell();
        cell.addEntity(wolf);
        cell.addEntity(wolf1);
        cell.addEntity(horse);
        cell.addEntity(grass);

        System.out.println(cell);

        System.out.println(cell.getEntitiesFor(AnimalType.WOLF));
        System.out.println(wolf.getEdibleTypes());


    }
}
