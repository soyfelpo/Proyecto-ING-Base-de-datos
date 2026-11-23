package com.cafeteria.core.domain;

public class Mesero extends Usuario {
    private final int idEmpleado;

    public Mesero(int id, String nombre, String email, String password, int idEmpleado) {
        super(id, nombre, email, password);
        this.idEmpleado = idEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void entregarPedido(Pedido pedido) {
        pedido.actualizarEstado("ENTREGADO");
    }
}
