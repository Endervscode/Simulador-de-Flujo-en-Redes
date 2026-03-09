package com.simulador.controlador;

import com.simulador.algoritmo.BFSBusqueda;
import com.simulador.algoritmo.FordFulkerson;
import com.simulador.modelo.Arista;
import com.simulador.modelo.Nodo;
import com.simulador.modelo.RedFlujo;
import com.simulador.vista.UIArista;
import com.simulador.vista.UINodo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetworkController {

    @FXML private Pane lienzo;
    @FXML private Label lblFlujoMaximo;
    @FXML private TextArea txtLogs;

    private RedFlujo<String> red;
    private int contadorNodos = 1;
    
    private UINodo nodoSeleccionado = null; // Para conectar aristas
    private UINodo uiFuente = null;
    private UINodo uiSumidero = null;
    private List<UIArista> aristasVisuales = new ArrayList<>();

    private volatile boolean simulacionEnCurso = false;
    private volatile boolean detenerSimulacion = false;

    @FXML
    public void initialize() {
        red = new RedFlujo<>();
        lienzo.setOnMouseClicked(this::manejarClicLienzo);
    }

    private void imprimirLog(String mensaje) {
        Platform.runLater(() -> {
            txtLogs.appendText("> " + mensaje + "\n");
            txtLogs.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void manejarClicLienzo(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getTarget() == lienzo && !simulacionEnCurso) {
            String id = "N" + contadorNodos++;
            Nodo<String> nuevoNodo = new Nodo<>(id);
            red.agregarNodo(nuevoNodo);

            // Inyectamos lambdas para delegar eventos desde la Vista al Controlador (Cumpliendo MVC)
            UINodo uiNodo = new UINodo(nuevoNodo, event.getX(), event.getY(), 
                this::definirFuente, this::definirSumidero, this::eliminarNodo);
            
            // Lógica para conectar aristas mediante selección de nodos
            uiNodo.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    if (nodoSeleccionado == null) {
                        nodoSeleccionado = uiNodo;
                        uiNodo.setStyle("-fx-effect: dropshadow(three-pass-box, cyan, 10, 0.8, 0, 0);");
                        imprimirLog("Seleccionado origen: " + uiNodo.getNodoModelo().getId());
                    } else if (nodoSeleccionado != uiNodo) {
                        crearArista(nodoSeleccionado, uiNodo);
                        nodoSeleccionado.setStyle(null);
                        nodoSeleccionado = null;
                    } else {
                        nodoSeleccionado.setStyle(null);
                        nodoSeleccionado = null;
                        imprimirLog("Selección cancelada.");
                    }
                    e.consume();
                }
            });

            lienzo.getChildren().add(uiNodo);
            imprimirLog("Nodo agregado: " + id);
        }
    }

    private void definirFuente(UINodo nodo) {
        if (uiFuente != null) uiFuente.setComoNormal();
        uiFuente = nodo;
        red.setFuente(nodo.getNodoModelo());
        nodo.setComoFuente();
        imprimirLog("Fuente definida: " + nodo.getNodoModelo().getId());
    }

    private void definirSumidero(UINodo nodo) {
        if (uiSumidero != null) uiSumidero.setComoNormal();
        uiSumidero = nodo;
        red.setSumidero(nodo.getNodoModelo());
        nodo.setComoSumidero();
        imprimirLog("Sumidero definido: " + nodo.getNodoModelo().getId());
    }

    private void eliminarNodo(UINodo nodo) {
        red.eliminarNodo(nodo.getNodoModelo());
        lienzo.getChildren().remove(nodo);
        aristasVisuales.removeIf(arista -> {
            if(arista.getAristaModelo().getOrigen().equals(nodo.getNodoModelo()) || 
               arista.getAristaModelo().getDestino().equals(nodo.getNodoModelo())) {
                lienzo.getChildren().remove(arista);
                return true;
            }
            return false;
        });
        if(uiFuente == nodo) uiFuente = null;
        if(uiSumidero == nodo) uiSumidero = null;
        imprimirLog("Nodo eliminado: " + nodo.getNodoModelo().getId());
    }

private void crearArista(UINodo origen, UINodo destino) {
    // Evitar arista duplicada (opcional, pero buena práctica)
    for (UIArista ui : aristasVisuales) {
        if (ui.getAristaModelo().getOrigen().equals(origen.getNodoModelo()) &&
            ui.getAristaModelo().getDestino().equals(destino.getNodoModelo())) {
            imprimirLog("Arista ya existe entre estos nodos.");
            return;
        }
    }

    TextInputDialog dialog = new TextInputDialog("10");
    dialog.setTitle("Nueva Arista");
    dialog.setHeaderText("Capacidad para " + origen.getNodoModelo().getId() + " → " + destino.getNodoModelo().getId());
    dialog.setContentText("Capacidad:");

    dialog.showAndWait().ifPresent(cap -> {
        try {
            double capacidad = Double.parseDouble(cap);
            if (capacidad <= 0) throw new NumberFormatException();

            red.agregarArista(origen.getNodoModelo(), destino.getNodoModelo(), capacidad);
            
            var lista = red.getGrafoAdyacencia().get(origen.getNodoModelo());
            var aristaModelo = lista.get(lista.size() - 1); // la recién agregada
            
            UIArista uiArista = new UIArista(origen, destino, aristaModelo);
            aristasVisuales.add(uiArista);
            lienzo.getChildren().add(0, uiArista); // fondo
            
            imprimirLog("Arista creada: " + origen.getNodoModelo().getId() + " → " + destino.getNodoModelo().getId() + " (cap: " + capacidad + ")");
        } catch (NumberFormatException ex) {
            imprimirLog("Capacidad inválida. Debe ser un número positivo.");
        }
    });
}

    @FXML private void limpiarGrafo() {
        detenerSimulacion = true;
        red.limpiarRed();
        lienzo.getChildren().clear();
        aristasVisuales.clear();
        uiFuente = null; uiSumidero = null;
        lblFlujoMaximo.setText("Flujo Máximo: 0");
        txtLogs.clear();
        imprimirLog("Grafo limpiado.");
    }

    @FXML private void reiniciarFlujos() {
        detenerSimulacion = true;
        red.reiniciarFlujos();
        aristasVisuales.forEach(ui -> { ui.resaltarCaminoAumento(false); ui.actualizarVista(); });
        lblFlujoMaximo.setText("Flujo Máximo: 0");
        imprimirLog("Flujos reiniciados a 0.");
    }

    @FXML private void ejecutarSimulacionAnimada() {
        if (!red.validarRed() || simulacionEnCurso) {
            imprimirLog("Error: Defina S y T primero."); return;
        }
        
        detenerSimulacion = false;
        simulacionEnCurso = true;
        red.reiniciarFlujos();
        imprimirLog("Iniciando Ford-Fulkerson...");

        // Arquitectura MVC: Ejecutamos el motor matemático en un hilo separado para no bloquear la UI
        new Thread(() -> {
            FordFulkerson<String> algoritmo = new FordFulkerson<>(new BFSBusqueda<>());
            
            while (!detenerSimulacion) {
                // 1. Ejecutar solo la matemática pura de una iteración
                List<Arista<String>> camino = algoritmo.ejecutarUnPaso(red);
                
                if (camino == null || detenerSimulacion) break;

                // 2. Animar el descubrimiento del camino en la UI, arista por arista
                try {
                    Platform.runLater(() -> imprimirLog("Camino de aumento encontrado: " + red.getFuente().getId()));
                    Thread.sleep(500); // Pausa inicial

                    for (Arista<String> aristaMod : camino) {
                        if(detenerSimulacion) break;

                        Platform.runLater(() -> {
                            aristasVisuales.stream()
                                .filter(ui -> ui.getAristaModelo() == aristaMod)
                                .findFirst().ifPresent(ui -> {
                                    ui.resaltarCaminoAumento(true);
                                    imprimirLog("  → " + ui.getAristaModelo().getDestino().getId());
                                });
                        });
                        Thread.sleep(700); // Pausa entre cada arista
                    }
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

                if(detenerSimulacion) break;

                // 3. Pausa para ver el camino completo y luego actualizar valores
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                if(detenerSimulacion) break;
                double flujoTotal = algoritmo.calcularFlujoTotalDesdeFuente(red);
                Platform.runLater(() -> {
                    aristasVisuales.forEach(ui -> { ui.resaltarCaminoAumento(false); ui.actualizarVista(); });
                    lblFlujoMaximo.setText("Flujo Máximo: " + flujoTotal);
                    imprimirLog("Flujo aumentado. Flujo total: " + String.format("%.2f", flujoTotal));
                });

                try { Thread.sleep(400); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            simulacionEnCurso = false;
            if(!detenerSimulacion) imprimirLog("Simulación completada.");
        }).start();
    }
}