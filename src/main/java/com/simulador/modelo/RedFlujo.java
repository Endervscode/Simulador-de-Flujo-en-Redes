package com.simulador.modelo;

import java.util.*;

public class RedFlujo<T> {
    private final Map<Nodo<T>, List<Arista<T>>> adyacencia = new HashMap<>();
    private Nodo<T> fuente;
    private Nodo<T> sumidero;

    public void agregarNodo(Nodo<T> nodo) { adyacencia.putIfAbsent(nodo, new ArrayList<>()); }

    public void agregarArista(Nodo<T> origen, Nodo<T> destino, double capacidad) {
        Arista<T> aristaAdelante = new Arista<>(origen, destino, capacidad);
        Arista<T> aristaAtras = new Arista<>(destino, origen, 0); 
        aristaAdelante.setAristaReversa(aristaAtras);
        aristaAtras.setAristaReversa(aristaAdelante);
        adyacencia.get(origen).add(aristaAdelante);
        adyacencia.get(destino).add(aristaAtras);
    }

    public void eliminarNodo(Nodo<T> nodo) {
        adyacencia.remove(nodo);
        adyacencia.values().forEach(lista -> lista.removeIf(a -> a.getDestino().equals(nodo)));
        if (fuente != null && fuente.equals(nodo)) fuente = null;
        if (sumidero != null && sumidero.equals(nodo)) sumidero = null;
    }

    public void limpiarRed() {
        adyacencia.clear();
        fuente = null;
        sumidero = null;
    }

    public void reiniciarFlujos() {
        adyacencia.values().forEach(lista -> lista.forEach(Arista::resetFlujo));
    }

    public List<Arista<T>> getAristasDe(Nodo<T> nodo) { return adyacencia.getOrDefault(nodo, new ArrayList<>()); }
    public Map<Nodo<T>, List<Arista<T>>> getGrafoAdyacencia() { return adyacencia; }
    public Nodo<T> getFuente() { return fuente; }
    public void setFuente(Nodo<T> fuente) { this.fuente = fuente; }
    public Nodo<T> getSumidero() { return sumidero; }
    public void setSumidero(Nodo<T> sumidero) { this.sumidero = sumidero; }
    public boolean validarRed() { return fuente != null && sumidero != null; }
}