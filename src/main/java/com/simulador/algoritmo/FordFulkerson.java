package com.simulador.algoritmo;

import com.simulador.modelo.Arista;
import com.simulador.modelo.RedFlujo;

import java.util.List;

public class FordFulkerson<T> implements AlgoritmoFlujo<T> {
    private final EstrategiaBusquedaCamino<T> buscador;

    public FordFulkerson(EstrategiaBusquedaCamino<T> buscador) {
        this.buscador = buscador;
    }

    // Ejecuta un solo paso para la animación dinámica
    public List<Arista<T>> ejecutarUnPaso(RedFlujo<T> red) {
        List<Arista<T>> camino = buscador.buscarCaminoAumento(red);
        if (camino != null) {
            double flujoCuelloBotella = Double.MAX_VALUE;
            for (Arista<T> arista : camino) {
                flujoCuelloBotella = Math.min(flujoCuelloBotella, arista.getCapacidadResidual());
            }
            for (Arista<T> arista : camino) {
                arista.agregarFlujo(flujoCuelloBotella);
            }
        }
        return camino; // Retorna el camino para colorearlo en la GUI
    }

    @Override
    public double calcularFlujoMaximo(RedFlujo<T> red) {
        if (!red.validarRed()) return 0.0;
        double flujoMaximo = 0.0;
        while (ejecutarUnPaso(red) != null) {
            // El flujo máximo se acumula indirectamente en la red
        }
        return calcularFlujoTotalDesdeFuente(red);
    }

    public double calcularFlujoTotalDesdeFuente(RedFlujo<T> red) {
        return red.getAristasDe(red.getFuente()).stream()
                .mapToDouble(Arista::getFlujo)
                .sum();
    }
}