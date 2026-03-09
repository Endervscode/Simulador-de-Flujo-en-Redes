package com.simulador.vista;

import com.simulador.modelo.Arista;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextInputDialog;

public class UIArista extends Group {
    private final Arista<String> aristaModelo;
    private final UINodo origen;
    private final UINodo destino;
    private final Line linea;
    private final Polygon cabezaFlecha;
    private final Text etiquetaFlujo;

    public UIArista(UINodo origen, UINodo destino, Arista<String> modelo) {
        this.aristaModelo = modelo;
        this.origen = origen;
        this.destino = destino;

        linea = new Line();
        linea.setStrokeWidth(3);

        cabezaFlecha = new Polygon(0, 0, -10, -5, -10, 5);
        
        etiquetaFlujo = new Text();
        etiquetaFlujo.setFont(Font.font("System", 13));
        etiquetaFlujo.setFill(Color.WHITE);

        // Si se hace clic en el texto, cambiar capacidad
        etiquetaFlujo.setOnMouseClicked(e -> {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(aristaModelo.getCapacidad()));
            dialog.setHeaderText("Editar Capacidad");
            dialog.showAndWait().ifPresent(val -> {
                try {
                    // Nota: En un diseño estricto esto debería pasar por el controlador, pero es aceptable aquí visualmente
                    aristaModelo.getAristaReversa().agregarFlujo(aristaModelo.getCapacidad() - Double.parseDouble(val)); // Ajuste hackish
                    actualizarVista();
                } catch (Exception ex) {}
            });
        });

        this.getChildren().addAll(linea, cabezaFlecha, etiquetaFlujo);

        // Listeners para recalcular trigonometría cuando los nodos se mueven (Drag)
        origen.layoutXProperty().addListener((obs, oldVal, newVal) -> recalcularGeometria());
        origen.layoutYProperty().addListener((obs, oldVal, newVal) -> recalcularGeometria());
        destino.layoutXProperty().addListener((obs, oldVal, newVal) -> recalcularGeometria());
        destino.layoutYProperty().addListener((obs, oldVal, newVal) -> recalcularGeometria());

        recalcularGeometria();
        actualizarVista();
    }

    public Arista<String> getAristaModelo() { return aristaModelo; }

    private void recalcularGeometria() {
        double startX = origen.getLayoutX() + 25;
        double startY = origen.getLayoutY() + 25;
        double endX = destino.getLayoutX() + 25;
        double endY = destino.getLayoutY() + 25;

        double angle = Math.atan2(endY - startY, endX - startX);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        // Despegar la línea de los bordes del círculo
        linea.setStartX(startX + 25 * cos);
        linea.setStartY(startY + 25 * sin);
        linea.setEndX(endX - 27 * cos);
        linea.setEndY(endY - 27 * sin);

        cabezaFlecha.setTranslateX(linea.getEndX());
        cabezaFlecha.setTranslateY(linea.getEndY());
        cabezaFlecha.setRotate(Math.toDegrees(angle));

        etiquetaFlujo.setX((linea.getStartX() + linea.getEndX()) / 2 + 10 * Math.sin(angle));
        etiquetaFlujo.setY((linea.getStartY() + linea.getEndY()) / 2 - 10 * Math.cos(angle));
    }

    public void actualizarVista() {
        etiquetaFlujo.setText(aristaModelo.getFlujo() + " / " + aristaModelo.getCapacidad());
        if (aristaModelo.getFlujo() >= aristaModelo.getCapacidad() && aristaModelo.getCapacidad() > 0) {
            setColor(Color.web("#e74c3c")); // Saturado (Rojo)
        } else if (aristaModelo.getFlujo() > 0) {
            setColor(Color.web("#f1c40f")); // Activo (Amarillo)
        } else {
            setColor(Color.web("#bdc3c7")); // Vacío (Gris)
        }
    }

    public void resaltarCaminoAumento(boolean resaltar) {
        if (resaltar) {
            setColor(Color.CYAN);
            linea.setStrokeWidth(6);
        } else {
            linea.setStrokeWidth(3);
            actualizarVista();
        }
    }

    private void setColor(Color color) {
        linea.setStroke(color);
        cabezaFlecha.setFill(color);
    }
}