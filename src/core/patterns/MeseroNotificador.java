package com.cafeteria.core.patterns;

public class MeseroNotificador implements Observador {
    @Override
    public void actualizar(String estado) {
        recibirAviso("Mesero — Estado actualizado del pedido: " + estado);
    }

    public void recibirAviso(String mensaje) {
        System.out.println(mensaje);
    }
}
