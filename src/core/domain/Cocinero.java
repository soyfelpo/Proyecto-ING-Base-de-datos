package com.cafeteria.core.domain;

public class Cocinero extends Usuario {
    private final int idEmpleado;

    public Cocinero(int id, String nombre, String email, String password, int idEmpleado) {
        super(id, nombre, email, password);
        this.idEmpleado = idEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void prepararPedido(Pedido pedido) {
        pedido.actualizarEstado("EN_PREPARACION");
    }

    public void actualizarEstadoPedido(Pedido pedido, String nuevoEstado) {
        pedido.actualizarEstado(nuevoEstado);
    }
}
