package com.cafeteria.core.services;

import com.cafeteria.core.domain.*;
import com.cafeteria.core.patterns.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class CafeteriaService {
    private final Menu menu;
    private final List<Pedido> pedidos;
    private final List<Promocion> promociones;
    private final Cajero cajero;
    private final Cocinero cocinero;
    private final Mesero mesero;
    private final Admin admin;

    private final Cocina cocinaObs;
    private final Caja cajaObs;
    private final MeseroNotificador meseroObs;

    private int secuenciaPedido = 1;
    private int secuenciaPago = 1;
    private int secuenciaReporte = 1;

    public CafeteriaService() {
        this.menu = new Menu(1);
        this.pedidos = new ArrayList<>();
        this.promociones = new ArrayList<>();

        this.cajero = new Cajero(1, "Laura Cajera", "caja@cafe.com", "1234", 101);
        this.cocinero = new Cocinero(2, "Carlos Chef", "cocina@cafe.com", "1234", 102);
        this.mesero = new Mesero(3, "Andres Mesero", "mesero@cafe.com", "1234", 103);
        this.admin = new Admin(4, "Admin Local", "admin@cafe.com", "1234", 104);

        this.cocinaObs = new Cocina();
        this.cajaObs = new Caja();
        this.meseroObs = new MeseroNotificador();

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        admin.gestionarMenu(menu, new Bebida(1, "Latte", 7000, "Café espresso con leche vaporizada", 20));
        admin.gestionarMenu(menu, new Bebida(2, "Jugo Natural", 6000, "Jugo en agua o leche", 15));
        admin.gestionarMenu(menu, new Plato(3, "Sándwich Artesanal", 14000, "Pan masa madre, jamón y queso", 12));
        admin.gestionarMenu(menu, new Plato(4, "Croissant", 5500, "Hojaldre recién horneado", 18));

        admin.gestionarPromociones(
                promociones,
                new Promocion(1, "Tarde Feliz (15% OFF)", 15.0, "00:00-23:59", EnumSet.allOf(DayOfWeek.class))
        );
    }

    public Menu getMenu() { return menu; }
    public List<Pedido> getPedidos() { return Collections.unmodifiableList(pedidos); }
    public List<Promocion> getPromociones() { return Collections.unmodifiableList(promociones); }
    public Cajero getCajero() { return cajero; }
    public Cocinero getCocinero() { return cocinero; }
    public Mesero getMesero() { return mesero; }
    public Admin getAdmin() { return admin; }

    public Producto aplicarAdicion(Producto base, int idAdicion, String nombre, double precioExtra) {
        return new AdicionExtra(idAdicion, nombre, precioExtra, base);
    }

    public Combo crearCombo(int id, String nombre, String desc, List<Producto> items, Double precioEspecial) {
        ComboBuilder builder = new ComboBuilder().definirDatos(id, nombre, desc);
        for (int i = 0; i < items.size(); i++) {
            builder.pasoAPaso("Paso " + (i + 1), items.get(i));
        }
        if (precioEspecial != null) {
            builder.conPrecioEspecial(precioEspecial);
        }
        Combo combo = builder.construirCombo();
        admin.gestionarMenu(menu, combo);
        return combo;
    }

    public Pedido crearPedido(List<Producto> productos, Promocion promoOpcional) {
        Pedido pedido = new Pedido(secuenciaPedido++);
        pedido.suscribir(cocinaObs);
        pedido.suscribir(cajaObs);
        pedido.suscribir(meseroObs);

        for (Producto p : productos) {
            pedido.agregarProducto(p, 1);
        }
        if (promoOpcional != null) {
            promoOpcional.aplicarDescuento(pedido, LocalDateTime.now());
        }
        pedidos.add(pedido);
        pedido.actualizarEstado("PENDIENTE");
        return pedido;
    }

    public void cambiarEstadoPedido(Pedido pedido, String nuevoEstado) {
        if ("EN_PREPARACION".equals(nuevoEstado)) {
            cocinero.prepararPedido(pedido);
        } else if ("ENTREGADO".equals(nuevoEstado)) {
            mesero.entregarPedido(pedido);
        } else {
            cocinero.actualizarEstadoPedido(pedido, nuevoEstado);
        }
    }

    public Pago cobrarPedido(Pedido pedido) {
        return cajero.registrarPago(secuenciaPago++, pedido);
    }

    public ReporteVentas cerrarCaja() {
        return cajero.cerrarCaja(secuenciaReporte++);
    }
}
