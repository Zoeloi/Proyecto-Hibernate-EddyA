module com.appregistros.proyectohibernate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.appregistros.proyectohibernate to javafx.fxml;
    exports com.appregistros.proyectohibernate;
}