module com.example.simulador {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires annotations;

    opens com.simulador to javafx.fxml;
    exports com.simulador;
    opens com.simulador.vista to javafx.fxml;
    opens com.simulador.controlador to javafx.fxml;
    exports com.simulador.modelo;
    exports com.simulador.algoritmo;
}