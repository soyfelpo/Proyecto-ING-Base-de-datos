package com.cafeteria.core.domain;

import com.cafeteria.core.patterns.AdicionDecorator;
import java.util.List;

public class Cliente extends Usuario {
    private final String telefono;
    private final String direccion;
    private final String metodoPago;

    public Cliente(int id, String nombre, String email, String password, String telefono, String direccion, String metodoPago) {
        super(id, nombre, email, password);
        this.telefono = telefono;
        this.direccion = direccion;
        this.metodoPago = metodoPago;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public List<Producto> consultarMenu(Menu menu) {
        return menu.obtenerProductos();
    }

    public Producto seleccionarProducto(Menu menu, int idProducto) {
        return menu.obtenerProductos().stream()
                .filter(p -> p.getIdProducto() == idProducto)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en el menú."));
    }

    public Producto personalizarProducto(Producto producto, AdicionDecorator adicion) {
        return adicion;
    }

    public Pedido realizarPedido(int idPedido, Producto producto, int cantidad) {
        Pedido pedido = new Pedido(idPedido);
        pedido.agregarProducto(producto, cantidad);
        return pedido;
    }
}
