package com.example.pk2.model;

public class HabitacionElementoList {
    String nombre, id, descripcion, imagen1, imagen2, imagen3, horas, precio, temperatura, sector;

    HabitacionElementoList (){}

    public HabitacionElementoList(String nombre, String id, String descripcion, String imagen1, String imagen2, String imagen3, String horas, String precio, String sector) {
        this.nombre = nombre;
        this.id = id;
        this.descripcion = descripcion;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
        this.imagen3 = imagen3;
        this.horas = horas;
        this.precio = precio;
        this.temperatura = temperatura;
        this.sector = sector;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen1() {
        return imagen1;
    }

    public void setImagen1(String imagen1) {
        this.imagen1 = imagen1;
    }

    public String getImagen2() {
        return imagen2;
    }

    public void setImagen2(String imagen2) {
        this.imagen2 = imagen2;
    }

    public String getImagen3() {
        return imagen3;
    }

    public void setImagen3(String imagen3) {
        this.imagen3 = imagen3;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "HabitacionElementoList{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen1='" + imagen1 + '\'' +
                ", imagen2='" + imagen2 + '\'' +
                ", imagen3='" + imagen3 + '\'' +
                ", horas='" + horas + '\'' +
                ", precio='" + precio + '\'' +
                ", temperatura='" + temperatura + '\'' +
                '}';
    }
}
