package com.example.pk2.model;

import java.io.Serializable;

public class MotelElementoList implements Serializable {
    private String nombre, direccion, imagen, id;

    public MotelElementoList(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MotelElementoList(String nombre, String direccion, String imagen, String id) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.imagen = imagen;
        this.id = id;
    }
}
