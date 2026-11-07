module com.appregistros.proyectohibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.appregistros.proyectohibernate to javafx.fxml;
    exports com.appregistros.proyectohibernate;
}