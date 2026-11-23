package com.cafeteria.core.domain;

public class Bebida extends Producto {
    public Bebida(int idProducto, String nombre, double precioBase, String descripcion, int stock) {
        super(idProducto, nombre, precioBase, descripcion, stock);
    }

    @Override
    public double getPrecio() {
        return getPrecioBase();
    }

    @Override
    public String getDescripcion() {
        return getNombre() + ": " + getDescripcionBase();
    }
}
