package com.simulador.modelo;

public class Arista<T> {
    private final Nodo<T> origen;
    private final Nodo<T> destino;
    private final double capacidad;
    private double flujo;
    private Arista<T> aristaReversa;

    public Arista(Nodo<T> origen, Nodo<T> destino, double capacidad) {
        this.origen = origen;
        this.destino = destino;
        this.capacidad = capacidad;
        this.flujo = 0.0;
    }

    public void setAristaReversa(Arista<T> aristaReversa) { this.aristaReversa = aristaReversa; }
    public Arista<T> getAristaReversa() { return aristaReversa; }
    public Nodo<T> getOrigen() { return origen; }
    public Nodo<T> getDestino() { return destino; }
    public double getCapacidad() { return capacidad; }
    public double getFlujo() { return flujo; }

    public double getCapacidadResidual() { return capacidad - flujo; }

    public void agregarFlujo(double delta) {
        this.flujo += delta;
        if (this.aristaReversa != null) {
            this.aristaReversa.flujo -= delta;
        }
    }

    // Nuevo: Permite reiniciar la simulación
    public void resetFlujo() {
        this.flujo = 0.0;
    }
}