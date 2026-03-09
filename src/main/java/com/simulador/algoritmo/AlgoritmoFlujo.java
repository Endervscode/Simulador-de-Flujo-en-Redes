package com.simulador.algoritmo;
import com.simulador.modelo.RedFlujo;

public interface AlgoritmoFlujo<T> {
    double calcularFlujoMaximo(RedFlujo<T> red);
}