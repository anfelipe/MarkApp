package com.uniajc.markapp.Clases;


public class Marcacion {

    private int documento;
    public String fecha;
    private String mac;
    public String horaEntrada;
    public String hora;
    private String direccion;
    private String latitud;
    private String longitid;
    private String codArea;

    public Marcacion(int documento, String fecha, String mac, String horaEntrada, String horaSalida,
                     String direccion, String latitud, String longitid, String codArea) {
        super();
        this.documento = documento;
        this.fecha = fecha;
        this.mac = mac;
        this.horaEntrada = horaEntrada;
        this.hora = horaSalida;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitid = longitid;
        this.codArea = codArea;
    }

    public Marcacion() {
        super();
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return hora;
    }

    public void setHoraSalida(String horaSalida) {
        this.hora = horaSalida;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitid() {
        return longitid;
    }

    public void setLongitid(String longitid) {
        this.longitid = longitid;
    }

    public String getCodArea() {
        return codArea;
    }

    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }
}
