package com.example.pk2.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    List<Mensaje_Texto> noMensajes;
    String id_Dueno;
    String id_Usuario;


    public String getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(String id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public Chat(String id_Reciever, String id_Usuario) {
        this.id_Dueno = id_Reciever;
        this.noMensajes = new ArrayList<>();
        this.id_Usuario = id_Usuario;
    }
    public Chat()
    {
        noMensajes = new ArrayList<>();
    }

    public void agregarMensaje(Mensaje_Texto mensaje)
    {
        if (noMensajes == null)
        {
            noMensajes = new ArrayList<>();
        }
        noMensajes.add(mensaje);
    }

    public List<Mensaje_Texto> getNoMensajes() {
        return noMensajes;
    }

    public String getId_Dueno() {
        return id_Dueno;
    }

    public void setId_Reciever(String id_Reciever) {
        this.id_Dueno = id_Reciever;
    }
}
