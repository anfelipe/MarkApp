package com.uniajc.markapp.Clases;


public class Persona {

    public String documento;
    public String nombre;
    public String apellido;

    public Persona() {
        super();
    }

    public Persona(String documento, String nombre, String apellido) {
        super();
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
