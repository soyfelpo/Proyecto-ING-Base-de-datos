package com.cafeteria.core.domain;

import java.util.List;

public class Admin extends Usuario {
    private final int idEmpleado;

    public Admin(int id, String nombre, String email, String password, int idEmpleado) {
        super(id, nombre, email, password);
        this.idEmpleado = idEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void gestionarMenu(Menu menu, Producto producto) {
        menu.agregarProducto(producto);
    }

    public void gestionarProductos(Producto producto, int cambioStock) {
        producto.actualizarStock(cambioStock);
    }

    public void gestionarPromociones(List<Promocion> promociones, Promocion nuevaPromocion) {
        promociones.add(nuevaPromocion);
    }
}
