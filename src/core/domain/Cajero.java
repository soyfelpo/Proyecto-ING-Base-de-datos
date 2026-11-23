package com.cafeteria.core.domain;

import java.util.ArrayList;
import java.util.List;

public class Cajero extends Usuario {
    private final int idEmpleado;
    private final List<Pago> pagosRegistrados;
    private boolean cajaAbierta;

    public Cajero(int id, String nombre, String email, String password, int idEmpleado) {
        super(id, nombre, email, password);
        this.idEmpleado = idEmpleado;
        this.pagosRegistrados = new ArrayList<>();
        this.cajaAbierta = true;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public boolean isCajaAbierta() {
        return cajaAbierta;
    }

    public Pago registrarPago(int idPago, Pedido pedido) {
        Pago pago = new Pago(idPago, pedido.getTotal());
        pago.procesarPago();
        pagosRegistrados.add(pago);
        pedido.actualizarEstado("PAGADO");
        return pago;
    }

    public ReporteVentas cerrarCaja(int idReporte) {
        this.cajaAbierta = false;
        return generarReporteVentas(idReporte);
    }

    public ReporteVentas generarReporteVentas(int idReporte) {
        ReporteVentas reporte = new ReporteVentas(idReporte);
        reporte.generar(pagosRegistrados);
        return reporte;
    }
}
