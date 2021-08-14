package com.example.movase.Models;

public class Amizade {
    private String id;
    private boolean amigo;
    private String idSolicitante;
    public Amizade(String id, boolean isAmigo, String idSolicitante) {
        this.id = id;
        this.amigo = isAmigo;
        this.idSolicitante = idSolicitante;
    }
    public Amizade(){

    }

    public Amizade(String id, boolean amigo) {
        this.id = id;
        this.amigo = amigo;
    }

    public String getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(String idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean isAmigo() {
        return amigo;
    }

    public void setAmigo(boolean amigo) {
        this.amigo = amigo;
    }
}
