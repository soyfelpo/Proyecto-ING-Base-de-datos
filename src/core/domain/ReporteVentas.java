package com.cafeteria.core.domain;

import java.util.Date;
import java.util.List;

public class ReporteVentas {
    private final int idReporte;
    private final Date fechaCreacion;
    private double totalVentas;

    public ReporteVentas(int idReporte) {
        this.idReporte = idReporte;
        this.fechaCreacion = new Date();
        this.totalVentas = 0.0;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public double generar(List<Pago> pagos) {
        this.totalVentas = pagos.stream()
                .filter(Pago::isProcesado)
                .mapToDouble(Pago::getMonto)
                .sum();
        return this.totalVentas;
    }
}
