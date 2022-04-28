package com.example.pk2.model;

import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Motel {
    String nombre;
    String id;
    String direccion;
    String imagen;
    String dirImagen;
    Integer numHab;
    List<HabitacionElementoList> habitaciones;

    public Motel(String nombre, String id, String direccion, String imagen, String dirImagen, Integer numHabv) {
        this.nombre = nombre;
        this.id = id;
        this.direccion = direccion;
        this.imagen = imagen;
        this.dirImagen = dirImagen;
        this.numHab = numHabv;
        this.habitaciones = new ArrayList<>();
    }

    public Motel() {
        this.habitaciones = new ArrayList<>();
    }

    public void agregarHabitacion(HabitacionElementoList habi){
        this.habitaciones.add(habi);
    }
    public List<HabitacionElementoList> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<HabitacionElementoList> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public String getDirImagen() {
        return dirImagen;
    }

    public Integer getNumHab() {
        return numHab;
    }

    public void setNumHab(Integer numHab) {
        this.numHab = numHab;
    }

    public void setDirImagen(String dirImagen) {
        this.dirImagen = dirImagen;
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

    public void setId(String id) {
        this.id = id;
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
}
