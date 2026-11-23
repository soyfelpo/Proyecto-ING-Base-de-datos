package com.cafeteria.core.patterns;

public class Caja implements Observador {
    @Override
    public void actualizar(String estado) {
        recibirAviso("Caja — Estado actualizado del pedido: " + estado);
    }

    public void recibirAviso(String mensaje) {
        System.out.println(mensaje);
    }

    public void registrarPago() {
        System.out.println("Caja — Lista para registrar pago.");
    }
}
