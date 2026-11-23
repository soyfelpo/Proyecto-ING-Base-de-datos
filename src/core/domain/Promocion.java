package com.cafeteria.core.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class Promocion {
    private final int idPromocion;
    private final String nombre;
    private final double descuento; // Porcentaje de descuento, ej. 15.0 = 15%
    private final String horarioActivo; // Ej. "15:00-17:00"
    private final Set<DayOfWeek> diasActivos;

    public Promocion(int idPromocion, String nombre, double descuento, String horarioActivo, Set<DayOfWeek> diasActivos) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descuento = descuento;
        this.horarioActivo = horarioActivo;
        this.diasActivos = diasActivos;
    }

    public int getIdPromocion() {
        return idPromocion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getDescuento() {
        return descuento;
    }

    public String getHorarioActivo() {
        return horarioActivo;
    }

    public boolean estaVigente(LocalDateTime momento) {
        if (diasActivos != null && !diasActivos.isEmpty() && !diasActivos.contains(momento.getDayOfWeek())) {
            return false;
        }
        if (horarioActivo == null || !horarioActivo.contains("-")) {
            return true;
        }
        String[] tramos = horarioActivo.split("-");
        LocalTime inicio = LocalTime.parse(tramos[0].trim());
        LocalTime fin = LocalTime.parse(tramos[1].trim());
        LocalTime horaActual = momento.toLocalTime();
        return !horaActual.isBefore(inicio) && !horaActual.isAfter(fin);
    }

    public double aplicarDescuento(Pedido pedido, LocalDateTime momento) {
        double subtotal = pedido.calcularTotal();
        if (!estaVigente(momento)) {
            pedido.setTotal(subtotal);
            return subtotal;
        }
        double montoDescuento = subtotal * (descuento / 100.0);
        double totalConDescuento = Math.max(0.0, subtotal - montoDescuento);
        pedido.setTotal(totalConDescuento);
        return totalConDescuento;
    }
}
