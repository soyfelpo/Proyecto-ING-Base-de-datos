package com.cafeteria.core.patterns;

import com.cafeteria.core.domain.Combo;
import com.cafeteria.core.domain.Producto;
import java.util.ArrayList;
import java.util.List;

public class ComboBuilder {
    private int idCombo;
    private String nombreCombo = "Combo";
    private String descripcionCombo = "Combo personalizado";
    private List<Producto> componentes = new ArrayList<>();
    private Double precioEspecial;

    public ComboBuilder definirDatos(int idCombo, String nombreCombo, String descripcionCombo) {
        this.idCombo = idCombo;
        this.nombreCombo = nombreCombo;
        this.descripcionCombo = descripcionCombo;
        return this;
    }

    public ComboBuilder pasoAPaso(String paso, Producto producto) {
        System.out.println("Paso [" + paso + "]: agregado -> " + producto.getDescripcion());
        this.componentes.add(producto);
        return this;
    }

    public ComboBuilder conPrecioEspecial(double precioEspecial) {
        this.precioEspecial = precioEspecial;
        return this;
    }

    public Combo construirCombo() {
        if (componentes.isEmpty()) {
            throw new IllegalStateException("Debe agregar al menos un producto para construir el combo.");
        }
        Combo combo = new Combo(idCombo, nombreCombo, descripcionCombo, componentes, precioEspecial);
        reiniciar();
        return combo;
    }

    public void reiniciar() {
        this.componentes = new ArrayList<>();
        this.precioEspecial = null;
    }
}  
