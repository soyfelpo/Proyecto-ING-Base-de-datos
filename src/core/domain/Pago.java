package com.cafeteria.core.domain;

import java.util.Date;

public class Pago {
    private final int idPago;
    private final double monto;
    private final Date fecha;
    private boolean procesado;

    public Pago(int idPago, double monto) {
        this.idPago = idPago;
        this.monto = monto;
        this.fecha = new Date();
        this.procesado = false;
    }

    public int getIdPago() {
        return idPago;
    }

    public double getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void procesarPago() {
        this.procesado = true;
    }
}
