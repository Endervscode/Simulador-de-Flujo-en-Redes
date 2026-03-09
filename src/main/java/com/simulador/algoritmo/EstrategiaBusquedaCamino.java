package com.simulador.algoritmo;
import com.simulador.modelo.Arista;
import com.simulador.modelo.Nodo;
import com.simulador.modelo.RedFlujo;
import java.util.List;

public interface EstrategiaBusquedaCamino<T> {
    List<Arista<T>> buscarCaminoAumento(RedFlujo<T> red);
}