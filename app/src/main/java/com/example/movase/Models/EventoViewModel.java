package com.example.movase.Models;

import android.os.Parcel;

public class EventoViewModel extends Evento {
    private String id;

    public EventoViewModel(Parcel in, String id) {
        super(in);
        this.id = id;
    }

    public EventoViewModel(String id) {
        this.id = id;
    }
    public EventoViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
