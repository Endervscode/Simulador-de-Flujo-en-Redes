package com.simulador.algoritmo;

import com.simulador.modelo.Arista;
import com.simulador.modelo.Nodo;
import com.simulador.modelo.RedFlujo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BFSBusqueda<T> implements EstrategiaBusquedaCamino<T> {
    @Override
    public List<Arista<T>> buscarCaminoAumento(RedFlujo<T> red) {
        Map<Nodo<T>, Arista<T>> padre = new HashMap<>();
        Queue<Nodo<T>> cola = new LinkedList<>();

        cola.add(red.getFuente());
        padre.put(red.getFuente(), null);

        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();

            for (Arista<T> arista : red.getAristasDe(actual)) {
                Nodo<T> vecino = arista.getDestino();

                if (!padre.containsKey(vecino) && arista.getCapacidadResidual() > 0) {
                    padre.put(vecino, arista);
                    cola.add(vecino);
                    if (vecino.equals(red.getSumidero())) {
                        return construirCamino(padre, red.getFuente(), red.getSumidero());
                    }
                }
            }
        }
        return null; // No hay más caminos
    }

    @NotNull
    private List<Arista<T>> construirCamino(Map<Nodo<T>, Arista<T>> padre, Nodo<T> fuente, Nodo<T> sumidero) {
            List<Arista<T>> camino = new ArrayList<>();
            Nodo<T> actual = sumidero;
            while (!actual.equals(fuente)) {
                Arista<T> arista = padre.get(actual);
                camino.add(arista);
                actual = arista.getOrigen();
            }
            Collections.reverse(camino);
            return camino;
    }
}
