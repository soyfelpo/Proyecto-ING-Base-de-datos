package com.cafeteria.core.patterns;

import com.cafeteria.core.domain.Producto;

public abstract class AdicionDecorator extends Producto {
    private final int idAdicion;
    private final String nombreAdicion;
    private final double precioExtra;
    protected final Producto productoEnvuelto;

    protected AdicionDecorator(int idAdicion, String nombreAdicion, double precioExtra, Producto productoEnvuelto) {
        super(
                productoEnvuelto.getIdProducto(),
                productoEnvuelto.getNombre(),
                productoEnvuelto.getPrecioBase(),
                productoEnvuelto.getDescripcionBase(),
                productoEnvuelto.getStock()
        );
        this.idAdicion = idAdicion;
        this.nombreAdicion = nombreAdicion;
        this.precioExtra = precioExtra;
        this.productoEnvuelto = productoEnvuelto;
    }

    public int getIdAdicion() {
        return idAdicion;
    }

    public String getNombreAdicion() {
        return nombreAdicion;
    }

    public double getPrecioExtra() {
        return precioExtra;
    }

    @Override
    public void actualizarStock(int cantidad) {
        productoEnvuelto.actualizarStock(cantidad);
    }
}
