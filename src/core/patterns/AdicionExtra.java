package com.cafeteria.core.patterns;

import com.cafeteria.core.domain.Producto;

public class AdicionExtra extends AdicionDecorator {
    public AdicionExtra(int idAdicion, String nombreAdicion, double precioExtra, Producto productoEnvuelto) {
        super(idAdicion, nombreAdicion, precioExtra, productoEnvuelto);
    }

    @Override
    public double getPrecio() {
        return productoEnvuelto.getPrecio() + getPrecioExtra();
    }

    @Override
    public String getDescripcion() {
        return productoEnvuelto.getDescripcion() + " + [" + getNombreAdicion() + " (+" + getPrecioExtra() + ")]";
    }
}
