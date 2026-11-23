package com.cafeteria.ui.cli;

import com.cafeteria.core.domain.*;
import com.cafeteria.core.services.CafeteriaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolaMain {
    private final CafeteriaService service;
    private final Scanner scanner;

    public ConsolaMain(CafeteriaService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean ejecutando = true;
        while (ejecutando) {
            mostrarMenuPrincipal();
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> verMenu();
                case "2" -> realizarPedidoConsola();
                case "3" -> armarComboConsola();
                case "4" -> cambiarEstadoPedidoConsola();
                case "5" -> cobrarYcierreConsola();
                case "6" -> agregarProductoAdminConsola();
                case "0" -> {
                    ejecutando = false;
                    System.out.println("Saliendo de la consola...");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== CAFETERÍA / RESTAURANTE — CONSOLA (RNF-01) ===");
        System.out.println("1. Consultar menú");
        System.out.println("2. Realizar pedido (con adición opcional y promoción)");
        System.out.println("3. Armar combo paso a paso");
        System.out.println("4. Cocinero / Mesero: Actualizar estado de pedido");
        System.out.println("5. Cajero: Registrar pago / Cerrar caja / Reporte");
        System.out.println("6. Admin: Agregar producto al menú");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opción: ");
    }

    private void verMenu() {
        System.out.println("\n--- MENÚ DISPONIBLE ---");
        List<Producto> productos = service.getMenu().obtenerProductos();
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            System.out.println((i + 1) + ". [" + p.getIdProducto() + "] " + p.getDescripcion() + " — $" + p.getPrecio());
        }
    }

    private void realizarPedidoConsola() {
        List<Producto> productos = service.getMenu().obtenerProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos en el menú.");
            return;
        }

        verMenu();
        System.out.print("Elige el número de producto: ");
        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (idx < 0 || idx >= productos.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        Producto seleccionado = productos.get(idx);

        System.out.print("¿Deseas agregar una adición opcional? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Nombre de la adición (ej. Extra queso, Leche almendra): ");
            String nombreExtra = scanner.nextLine().trim();
            System.out.print("Precio extra: ");
            double precioExtra = Double.parseDouble(scanner.nextLine().trim());
            seleccionado = service.aplicarAdicion(seleccionado, 100 + idx, nombreExtra, precioExtra);
        }

        Promocion promoElegida = null;
        if (!service.getPromociones().isEmpty()) {
            System.out.print("¿Aplicar promoción '" + service.getPromociones().get(0).getNombre() + "' si está vigente? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                promoElegida = service.getPromociones().get(0);
            }
        }

        Pedido pedido = service.crearPedido(List.of(seleccionado), promoElegida);
        System.out.println("Pedido #" + pedido.getIdPedido() + " creado. Total: $" + pedido.getTotal() + " | Estado: " + pedido.getEstado());
    }

    private void armarComboConsola() {
        List<Producto> disponibles = service.getMenu().obtenerProductos();
        if (disponibles.size() < 2) {
            System.out.println("Se necesitan al menos 2 productos en el menú para armar un combo.");
            return;
        }

        System.out.print("Nombre del combo: ");
        String nombreCombo = scanner.nextLine().trim();
        System.out.print("Descripción del combo: ");
        String descCombo = scanner.nextLine().trim();

        List<Producto> itemsCombo = new ArrayList<>();
        boolean seguir = true;
        while (seguir) {
            verMenu();
            System.out.print("Elige producto para el siguiente paso del combo: ");
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < disponibles.size()) {
                itemsCombo.add(disponibles.get(idx));
            }
            System.out.print("¿Agregar otro paso al combo? (s/n): ");
            seguir = scanner.nextLine().trim().equalsIgnoreCase("s");
        }

        if (itemsCombo.isEmpty()) {
            System.out.println("No se agregaron productos al combo.");
            return;
        }

        Combo combo = service.crearCombo(
                disponibles.size() + 1,
                nombreCombo,
                descCombo,
                itemsCombo,
                null
        );
        System.out.println("Combo creado y agregado al menú: " + combo.getDescripcion() + " — $" + combo.getPrecio());
    }

    private void cambiarEstadoPedidoConsola() {
        listarPedidos();
        List<Pedido> pedidos = service.getPedidos();
        if (pedidos.isEmpty()) return;

        System.out.print("Número de índice del pedido (1.." + pedidos.size() + "): ");
        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (idx < 0 || idx >= pedidos.size()) {
            System.out.println("Pedido inválido.");
            return;
        }

        System.out.println("Elige nuevo estado: 1) EN_PREPARACION  2) LISTO  3) ENTREGADO");
        String op = scanner.nextLine().trim();
        String nuevoEstado = switch (op) {
            case "1" -> "EN_PREPARACION";
            case "2" -> "LISTO";
            case "3" -> "ENTREGADO";
            default -> null;
        };

        if (nuevoEstado == null) {
            System.out.println("Estado no válido.");
            return;
        }

        service.cambiarEstadoPedido(pedidos.get(idx), nuevoEstado);
    }

    private void cobrarYcierreConsola() {
        listarPedidos();
        List<Pedido> pedidos = service.getPedidos();

        System.out.println("1. Registrar pago de un pedido");
        System.out.println("2. Cerrar caja y generar reporte de ventas");
        System.out.print("Opción: ");
        String op = scanner.nextLine().trim();

        if ("1".equals(op) && !pedidos.isEmpty()) {
            System.out.print("Número de índice del pedido a cobrar (1.." + pedidos.size() + "): ");
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < pedidos.size()) {
                Pago pago = service.cobrarPedido(pedidos.get(idx));
                System.out.println("Pago #" + pago.getIdPago() + " procesado por $" + pago.getMonto());
            }
        } else if ("2".equals(op)) {
            ReporteVentas reporte = service.cerrarCaja();
            System.out.println("Cierre de caja completado — Reporte #" + reporte.getIdReporte()
                    + " | Total ventas: $" + reporte.getTotalVentas());
        }
    }

    private void agregarProductoAdminConsola() {
        System.out.print("Tipo (1 = Bebida, 2 = Plato): ");
        String tipo = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Precio base: ");
        double precio = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Descripción: ");
        String desc = scanner.nextLine().trim();

        int nuevoId = service.getMenu().obtenerProductos().size() + 1;
        Producto nuevo = "2".equals(tipo)
                ? new Plato(nuevoId, nombre, precio, desc, 15)
                : new Bebida(nuevoId, nombre, precio, desc, 15);

        service.getAdmin().gestionarMenu(service.getMenu(), nuevo);
        System.out.println("Producto agregado al menú correctamente.");
    }

    private void listarPedidos() {
        List<Pedido> pedidos = service.getPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados aún.");
            return;
        }
        System.out.println("\n--- PEDIDOS ---");
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            System.out.println((i + 1) + ". Pedido #" + p.getIdPedido()
                    + " | Estado: " + p.getEstado()
                    + " | Total: $" + p.getTotal());
        }
    }

    public static void main(String[] args) {
        CafeteriaService service = new CafeteriaService();
        new ConsolaMain(service).iniciar();
    }
}
