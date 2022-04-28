package com.example.pk2.model;

public class Mensaje_Texto {
    String contenidoMensaje;
    String dueno;
    String nombreDueno;
    long createdAt;
    public Mensaje_Texto(){}

    public Mensaje_Texto(String contenidoMensaje, String dueno, long createdAt, String nombreDueno) {
        this.contenidoMensaje = contenidoMensaje;
        this.dueno = dueno;
        this.createdAt = createdAt;
        this.nombreDueno = nombreDueno;
    }

    public String getNombreDueno() {
        return nombreDueno;
    }

    public void setNombreDueno(String nombreDueno) {
        this.nombreDueno = nombreDueno;
    }

    public String getContenidoMensaje() {
        return contenidoMensaje;
    }

    public void setContenidoMensaje(String contenidoMensaje) {
        this.contenidoMensaje = contenidoMensaje;
    }

    public String getDueno() {
        return dueno;
    }

    public void setDueno(String dueno) {
        this.dueno = dueno;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
