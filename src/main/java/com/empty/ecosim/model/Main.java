package com.empty.ecosim.model;

import com.empty.ecosim.model.entity.organism.animals.AnimalType;
import com.empty.ecosim.model.entity.organism.animals.factory.AnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.factory.SimpleAnimalFactory;
import com.empty.ecosim.model.entity.organism.animals.herbivores.Horse;
import com.empty.ecosim.model.entity.organism.animals.herbivores.Sheep;
import com.empty.ecosim.model.entity.organism.animals.predators.Wolf;
import com.empty.ecosim.model.entity.island.Cell;
import com.empty.ecosim.model.entity.organism.plants.Grass;
import com.empty.ecosim.model.entity.organism.plants.factory.PlantFactory;
import com.empty.ecosim.model.entity.organism.plants.factory.SimplePlantFactory;

import static com.empty.ecosim.model.entity.organism.animals.AnimalType.*;
import static com.empty.ecosim.model.entity.organism.plants.PlantType.GRASS;

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {

        AnimalFactory animalFactory = new SimpleAnimalFactory();
        PlantFactory plantFactory = new SimplePlantFactory();

        Wolf wolf = (Wolf) animalFactory.createAnimal(WOLF);
        Sheep sheep = (Sheep) animalFactory.createAnimal(SHEEP);
        Horse horse = (Horse) animalFactory.createAnimal(HORSE);
        Grass grass = (Grass) plantFactory.createPlant(GRASS);


        Cell cell = new Cell();
        cell.addEntity(wolf);
        cell.addEntity(sheep);
        cell.addEntity(horse);
        cell.addEntity(grass);

        System.out.println(cell);

        System.out.println(cell.getEntitiesFor(AnimalType.WOLF));
        wolf.setSatiety(4);
        wolf.findFood(cell);
        AnimalType.valueOf(wolf.getType().name());

    }
}
