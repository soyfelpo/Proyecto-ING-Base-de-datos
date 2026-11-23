package com.cafeteria.core.patterns;

public interface Observador {
    void actualizar(String estado);

    default void actualizarPedido(int idPedido, String estado) {
        actualizar(estado);
    }
}
