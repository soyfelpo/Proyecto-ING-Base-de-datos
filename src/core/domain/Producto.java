package com.cafeteria.core.domain;

public abstract class Producto {
    private final int idProducto;
    private final String nombre;
    private final double precioBase;
    private final String descripcion;
    private int stock;

    protected Producto(int idProducto, String nombre, double precioBase, String descripcion, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.descripcion = descripcion;
        this.stock = Math.max(0, stock);
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public String getDescripcionBase() {
        return descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void actualizarStock(int cantidad) {
        this.stock = Math.max(0, this.stock + cantidad);
    }

    public abstract double getPrecio();

    public abstract String getDescripcion();
}
