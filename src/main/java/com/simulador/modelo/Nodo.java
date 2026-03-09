package com.simulador.modelo;

import java.util.Objects;

public class Nodo<T> {
    private final T id;
    private boolean activo = true;

    public Nodo(T id) { this.id = id; }

    public T getId() { return id; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nodo<?> nodo = (Nodo<?>) o;
        return Objects.equals(id, nodo.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return id.toString(); }
}