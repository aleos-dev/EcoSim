module com.empty.ecosim {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.empty.ecosim.model.entity.organism.animals;
    opens com.empty.ecosim.model.entity.organism.plants;
    opens com.empty.ecosim.model.entity.island;
    opens com.empty.ecosim to javafx.fxml;
    exports com.empty.ecosim;
    opens com.empty.ecosim.model.configuration;
    opens com.empty.ecosim.model.entity.organism.animals.predators;
    opens com.empty.ecosim.model.entity.organism.animals.herbivores;
    opens com.empty.ecosim.model.entity.organism.animals.factory;
    opens com.empty.ecosim.model.entity.organism;
}