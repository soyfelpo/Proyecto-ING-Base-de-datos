package com.cafeteria.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Menu {
    private final int idMenu;
    private Date fechaActualizacion;
    private final List<Producto> productos;

    public Menu(int idMenu) {
        this.idMenu = idMenu;
        this.fechaActualizacion = new Date();
        this.productos = new ArrayList<>();
    }

    public int getIdMenu() {
        return idMenu;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
        this.fechaActualizacion = new Date();
    }

    public List<Producto> obtenerProductos() {
        return Collections.unmodifiableList(productos);
    }
}
