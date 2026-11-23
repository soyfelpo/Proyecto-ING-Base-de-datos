package com.cafeteria.core.domain;

public abstract class Usuario {
    private final int id;
    private final String nombre;
    private final String email;
    private final String password;
    private boolean sesionActiva;

    protected Usuario(int id, String nombre, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.sesionActiva = false;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSesionActiva() {
        return sesionActiva;
    }

    public void iniciarSesion() {
        this.sesionActiva = true;
    }

    public void cerrarSesion() {
        this.sesionActiva = false;
    }
}
