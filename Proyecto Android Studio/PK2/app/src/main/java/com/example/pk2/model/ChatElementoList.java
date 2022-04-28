package com.example.pk2.model;

public class ChatElementoList
{
    String id_Reciever;
    String name_Reciever;
    String lastName_Reciever;

    public ChatElementoList(String id_Reciever, String name_Reciever, String lastName_Reciever) {
        this.id_Reciever = id_Reciever;
        this.name_Reciever = name_Reciever;
        this.lastName_Reciever = lastName_Reciever;
    }

    public String getId_Reciever() {
        return id_Reciever;
    }

    public void setId_Reciever(String id_Reciever) {
        this.id_Reciever = id_Reciever;
    }

    public String getName_Reciever() {
        return name_Reciever;
    }

    public void setName_Reciever(String name_Reciever) {
        this.name_Reciever = name_Reciever;
    }

    public String getLastName_Reciever() {
        return lastName_Reciever;
    }

    public void setLastName_Reciever(String lastName_Reciever) {
        this.lastName_Reciever = lastName_Reciever;
    }
}
