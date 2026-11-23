package com.cafeteria.ui.gui;

import com.cafeteria.core.domain.*;
import com.cafeteria.core.services.CafeteriaService;
import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private final CafeteriaService service;
    private final DefaultListModel<String> modeloMenu = new DefaultListModel<>();
    private final DefaultListModel<String> modeloCarrito = new DefaultListModel<>();
    private final DefaultListModel<String> modeloPedidos = new DefaultListModel<>();
    private final List<Producto> carritoActual = new ArrayList<>();
    private final JTextArea consolaAvisos = new JTextArea(6, 50);
    private final JLabel lblTotalCarrito = new JLabel("Total carrito: $0.0");

    public VentanaPrincipal(CafeteriaService service) {
        this.service = service;
        setTitle("Cafetería & Restaurante — Sistema de Pedidos (Software II)");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        redirigirAvisosATextArea();

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("1. Cliente (Pedidos y Combos)", crearPanelCliente());
        pestanas.addTab("2. Cocina (Estados)", crearPanelCocina());
        pestanas.addTab("3. Caja y Reportes", crearPanelCaja());
        pestanas.addTab("4. Admin (Menú y Promos)", crearPanelAdmin());

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Avisos Automáticos en Tiempo Real (Patrón Observer)"));
        consolaAvisos.setEditable(false);
        panelInferior.add(new JScrollPane(consolaAvisos), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(pestanas, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        refrescarTodo();
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JList<String> listaMenu = new JList<>(modeloMenu);
        JList<String> listaCarrito = new JList<>(modeloCarrito);

        JButton btnAgregar = new JButton("Agregar producto");
        JButton btnAgregarExtra = new JButton("Agregar + Extra ($1.500)");
        JButton btnArmarCombo = new JButton("Armar Combo con Carrito");
        JButton btnConfirmarPedido = new JButton("Confirmar Pedido");

        btnAgregar.addActionListener(e -> {
            int idx = listaMenu.getSelectedIndex();
            if (idx >= 0) {
                Producto p = service.getMenu().obtenerProductos().get(idx);
                carritoActual.add(p);
                actualizarCarrito();
            }
        });

        btnAgregarExtra.addActionListener(e -> {
            int idx = listaMenu.getSelectedIndex();
            if (idx >= 0) {
                String nombreExtra = JOptionPane.showInputDialog(this, "Nombre de la adición (ej. Extra queso, Leche almendra):");
                if (nombreExtra != null && !nombreExtra.isBlank()) {
                    Producto base = service.getMenu().obtenerProductos().get(idx);
                    Producto decorado = service.aplicarAdicion(base, carritoActual.size() + 1, nombreExtra, 1500);
                    carritoActual.add(decorado);
                    actualizarCarrito();
                }
            }
        });

        btnArmarCombo.addActionListener(e -> {
            if (carritoActual.size() < 2) {
                JOptionPane.showMessageDialog(this, "Agrega al menos 2 productos al carrito para armar un combo paso a paso.");
                return;
            }
            String nombreCombo = JOptionPane.showInputDialog(this, "Nombre del combo:");
            if (nombreCombo != null && !nombreCombo.isBlank()) {
                double suma = carritoActual.stream().mapToDouble(Producto::getPrecio).sum();
                Combo combo = service.crearCombo(
                        service.getMenu().obtenerProductos().size() + 1,
                        nombreCombo,
                        "Combo armado paso a paso",
                        new ArrayList<>(carritoActual),
                        suma * 0.90
                );
                carritoActual.clear();
                carritoActual.add(combo);
                actualizarCarrito();
                refrescarTodo();
            }
        });

        btnConfirmarPedido.addActionListener(e -> {
            if (carritoActual.isEmpty()) return;
            Promocion promo = service.getPromociones().isEmpty() ? null : service.getPromociones().get(0);
            int aplicarPromo = JOptionPane.showConfirmDialog(this, "¿Aplicar promoción vigente si cumple horario?", "Promoción", JOptionPane.YES_NO_OPTION);
            service.crearPedido(new ArrayList<>(carritoActual), aplicarPromo == JOptionPane.YES_OPTION ? promo : null);
            carritoActual.clear();
            actualizarCarrito();
            refrescarTodo();
        });

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar);
        botones.add(btnAgregarExtra);
        botones.add(btnArmarCombo);
        botones.add(btnConfirmarPedido);
        botones.add(lblTotalCarrito);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                envolverConTitulo(new JScrollPane(listaMenu), "Menú Disponible"),
                envolverConTitulo(new JScrollPane(listaCarrito), "Pedido Actual"));
        split.setDividerLocation(430);

        panel.add(split, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCocina() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JList<String> listaPedidos = new JList<>(modeloPedidos);

        JButton btnPreparar = new JButton("Pasar a EN_PREPARACION");
        JButton btnListo = new JButton("Pasar a LISTO");
        JButton btnEntregar = new JButton("Mesero: Marcar ENTREGADO");

        btnPreparar.addActionListener(e -> cambiarEstadoSeleccionado(listaPedidos.getSelectedIndex(), "EN_PREPARACION"));
        btnListo.addActionListener(e -> cambiarEstadoSeleccionado(listaPedidos.getSelectedIndex(), "LISTO"));
        btnEntregar.addActionListener(e -> cambiarEstadoSeleccionado(listaPedidos.getSelectedIndex(), "ENTREGADO"));

        JPanel sur = new JPanel();
        sur.add(btnPreparar);
        sur.add(btnListo);
        sur.add(btnEntregar);

        panel.add(envolverConTitulo(new JScrollPane(listaPedidos), "Pedidos Activos"), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCaja() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JList<String> listaPedidos = new JList<>(modeloPedidos);
        JTextArea areaReporte = new JTextArea();
        areaReporte.setEditable(false);

        JButton btnCobrar = new JButton("Registrar Pago del Pedido");
        JButton btnCerrarCaja = new JButton("Cerrar Caja y Generar Reporte");

        btnCobrar.addActionListener(e -> {
            int idx = listaPedidos.getSelectedIndex();
            if (idx >= 0) {
                Pedido pedido = service.getPedidos().get(idx);
                Pago pago = service.cobrarPedido(pedido);
                areaReporte.append("Pago #" + pago.getIdPago() + " registrado por $" + pago.getMonto() + "\n");
                refrescarTodo();
            }
        });

        btnCerrarCaja.addActionListener(e -> {
            ReporteVentas reporte = service.cerrarCaja();
            areaReporte.append("=== CIERRE DE CAJA — Reporte #" + reporte.getIdReporte()
                    + " | Total Ventas: $" + reporte.getTotalVentas() + " ===\n");
        });

        JPanel sur = new JPanel();
        sur.add(btnCobrar);
        sur.add(btnCerrarCaja);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                envolverConTitulo(new JScrollPane(listaPedidos), "Pedidos para Cobro"),
                envolverConTitulo(new JScrollPane(areaReporte), "Movimientos y Cierre de Caja"));
        split.setDividerLocation(430);

        panel.add(split, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelAdmin() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Bebida", "Plato"});
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtDesc = new JTextField();
        JButton btnCrearProducto = new JButton("Agregar al Menú");
        JButton btnCrearPromo = new JButton("Agregar Promo 20% Todo el Día");

        btnCrearProducto.addActionListener(e -> {
            try {
                int id = service.getMenu().obtenerProductos().size() + 1;
                String nombre = txtNombre.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                String desc = txtDesc.getText();
                Producto p = "Bebida".equals(cmbTipo.getSelectedItem())
                        ? new Bebida(id, nombre, precio, desc, 10)
                        : new Plato(id, nombre, precio, desc, 10);
                service.getAdmin().gestionarMenu(service.getMenu(), p);
                refrescarTodo();
                JOptionPane.showMessageDialog(this, "Producto agregado al menú.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifica los datos ingresados.");
            }
        });

        btnCrearPromo.addActionListener(e -> {
            service.getAdmin().gestionarPromociones(
                    new ArrayList<>(service.getPromociones()),
                    new Promocion(2, "Promo Admin 20%", 20.0, "00:00-23:59", EnumSet.allOf(DayOfWeek.class))
            );
            JOptionPane.showMessageDialog(this, "Promoción registrada.");
        });

        panel.add(new JLabel("Tipo de producto:"));
        panel.add(cmbTipo);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Precio base:"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDesc);
        panel.add(btnCrearProducto);
        panel.add(btnCrearPromo);
        return panel;
    }

    private void cambiarEstadoSeleccionado(int idx, String estado) {
        if (idx >= 0) {
            Pedido pedido = service.getPedidos().get(idx);
            service.cambiarEstadoPedido(pedido, estado);
            refrescarTodo();
        }
    }

    private void actualizarCarrito() {
        modeloCarrito.clear();
        double total = 0;
        for (Producto p : carritoActual) {
            modeloCarrito.addElement(p.getDescripcion() + " — $" + p.getPrecio());
            total += p.getPrecio();
        }
        lblTotalCarrito.setText("Total carrito: $" + total);
    }

    private void refrescarTodo() {
        modeloMenu.clear();
        for (Producto p : service.getMenu().obtenerProductos()) {
            modeloMenu.addElement("#" + p.getIdProducto() + " | " + p.getDescripcion() + " — $" + p.getPrecio());
        }
        modeloPedidos.clear();
        for (Pedido ped : service.getPedidos()) {
            modeloPedidos.addElement("Pedido #" + ped.getIdPedido() + " | Estado: " + ped.getEstado() + " | Total: $" + ped.getTotal());
        }
    }

    private JPanel envolverConTitulo(JComponent comp, String titulo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void redirigirAvisosATextArea() {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                consolaAvisos.append(String.valueOf((char) b));
            }
        });
        System.setOut(ps);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CafeteriaService service = new CafeteriaService();
            new VentanaPrincipal(service).setVisible(true);
        });
    }
}
