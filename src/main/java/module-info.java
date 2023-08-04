module com.empty.ecosim {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.empty.ecosim.model.animals;
    opens com.empty.ecosim.model.plants;
    opens com.empty.ecosim to javafx.fxml;
    exports com.empty.ecosim;
    opens com.empty.ecosim.model.configuration;
    opens com.empty.ecosim.model.animals.predators;
    opens com.empty.ecosim.model.animals.herbivores;
    opens com.empty.ecosim.model.animals.factory;
}