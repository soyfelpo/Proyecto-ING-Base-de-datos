package com.cafeteria.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Combo extends Producto {
    private final List<Producto> productos;
    private final Double precioEspecial;

    public Combo(int idProducto, String nombre, String descripcion, List<Producto> productos, Double precioEspecial) {
        super(idProducto, nombre, calcularPrecioBase(productos, precioEspecial), descripcion, 0);
        this.productos = new ArrayList<>(productos);
        this.precioEspecial = precioEspecial;
    }

    private static double calcularPrecioBase(List<Producto> productos, Double precioEspecial) {
        if (precioEspecial != null) {
            return precioEspecial;
        }
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    @Override
    public double getPrecio() {
        if (precioEspecial != null) {
            return precioEspecial;
        }
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    @Override
    public String getDescripcion() {
        String detalle = productos.stream()
                .map(Producto::getDescripcion)
                .collect(Collectors.joining(" + "));
        return getNombre() + " (" + getDescripcionBase() + ") — Incluye: " + detalle;
    }
}
