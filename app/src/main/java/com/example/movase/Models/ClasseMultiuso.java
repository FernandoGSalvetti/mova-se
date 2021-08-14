package com.example.movase.Models;

public class ClasseMultiuso {
    private String id;
    public ClasseMultiuso(){}
    public ClasseMultiuso(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
