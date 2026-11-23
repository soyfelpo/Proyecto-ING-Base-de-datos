package com.cafeteria.core.domain;

import com.cafeteria.core.patterns.Observador;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Pedido {
    private final int idPedido;
    private final Date fecha;
    private String estado;
    private double total;
    private final List<DetallePedido> detalles;
    private final List<Observador> observadores;

    public Pedido(int idPedido) {
        this.idPedido = idPedido;
        this.fecha = new Date();
        this.estado = "PENDIENTE";
        this.total = 0.0;
        this.detalles = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void suscribir(Observador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(Observador observador) {
        observadores.remove(observador);
    }

    public void agregarProducto(Producto producto, int cantidad) {
        detalles.add(new DetallePedido(producto, cantidad));
        calcularTotal();
    }

    public double calcularTotal() {
        double suma = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
        this.total = suma;
        return this.total;
    }

    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        notificarObservadores();
    }

    private void notificarObservadores() {
        for (Observador observador : observadores) {
            observador.actualizar(this.estado);
        }
    }
}
