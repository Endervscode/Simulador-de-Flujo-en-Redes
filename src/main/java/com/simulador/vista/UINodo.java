package com.simulador.vista;

import com.simulador.modelo.Nodo;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class UINodo extends StackPane {
    private final Nodo<String> nodoModelo;
    private final Circle circulo;
    private final Text etiqueta;
    private double offsetX, offsetY;

    public UINodo(Nodo<String> nodoModelo, double x, double y, 
                  Consumer<UINodo> onSetFuente, Consumer<UINodo> onSetSumidero, 
                  Consumer<UINodo> onEliminar) {
        this.nodoModelo = nodoModelo;
        this.setLayoutX(x - 25);
        this.setLayoutY(y - 25);

        this.circulo = new Circle(25, Color.web("#3498db"));
        circulo.setStroke(Color.WHITE);
        circulo.setStrokeWidth(2);

        this.etiqueta = new Text(nodoModelo.getId());
        etiqueta.setFill(Color.WHITE);
        etiqueta.setFont(Font.font("System", 14));

        this.getChildren().addAll(circulo, etiqueta);

        // Arrastrar y Soltar (Drag & Drop)
        this.setOnMousePressed(e -> {
            offsetX = e.getSceneX() - getLayoutX();
            offsetY = e.getSceneY() - getLayoutY();
            e.consume();
        });

        this.setOnMouseDragged(e -> {
            setLayoutX(e.getSceneX() - offsetX);
            setLayoutY(e.getSceneY() - offsetY);
            e.consume();
        });

        // Menú Contextual
        ContextMenu menu = new ContextMenu();
        MenuItem mnuFuente = new MenuItem("Definir como Fuente (S)");
        MenuItem mnuSumidero = new MenuItem("Definir como Sumidero (T)");
        MenuItem mnuEliminar = new MenuItem("Eliminar Nodo");

        mnuFuente.setOnAction(e -> onSetFuente.accept(this));
        mnuSumidero.setOnAction(e -> onSetSumidero.accept(this));
        mnuEliminar.setOnAction(e -> onEliminar.accept(this));

        menu.getItems().addAll(mnuFuente, mnuSumidero, new SeparatorMenuItem(), mnuEliminar);

        this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
    }

    public Nodo<String> getNodoModelo() { return nodoModelo; }

    public void setComoNormal() { 
        circulo.setFill(Color.web("#3498db"));
        etiqueta.setText(nodoModelo.getId());
    }
    public void setComoFuente() { 
        circulo.setFill(Color.web("#2ecc71")); 
        etiqueta.setText("S");
    }
    public void setComoSumidero() { 
        circulo.setFill(Color.web("#e74c3c")); 
        etiqueta.setText("T");
    }
}