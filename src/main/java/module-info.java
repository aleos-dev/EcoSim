module com.empty.ecosim {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.empty.ecosim to javafx.fxml;
    exports com.empty.ecosim;
}