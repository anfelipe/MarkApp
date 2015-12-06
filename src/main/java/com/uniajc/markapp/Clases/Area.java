package com.uniajc.markapp.Clases;


public class Area {

    public Integer codigo;
    public String nombre;

    public Area(){
        super();
    }

    public Area(int codigo, String nombre){
        super();
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
