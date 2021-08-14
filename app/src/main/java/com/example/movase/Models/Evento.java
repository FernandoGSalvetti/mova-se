package com.example.movase.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Evento implements Parcelable {
    private String nome;
    private String modalidade;
    private String data;
    private String horario;
    private String endereco;
    private String tipoDeEvento;
    private String idCriadorDoEvento;

    protected Evento(Parcel in) {
        nome = in.readString();
        modalidade = in.readString();
        horario = in.readString();
        endereco = in.readString();
        tipoDeEvento = in.readString();
    }
    public Evento(){

    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(modalidade);
        dest.writeString(horario);
        dest.writeString(endereco);
        dest.writeString(tipoDeEvento);
        dest.writeString(idCriadorDoEvento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoDeEvento() {
        return tipoDeEvento;
    }

    public void setTipoDeEvento(String tipoDeEvento) {
        this.tipoDeEvento = tipoDeEvento;
    }

    public String getIdCriadorDoEvento() {
        return idCriadorDoEvento;
    }

    public void setIdCriadorDoEvento(String idCriadorDoEvento) {
        this.idCriadorDoEvento = idCriadorDoEvento;
    }

    @Override
    public String toString() {
        return "Nome do evento: " + this.nome +
                "\nModalidade: " + this.modalidade +
                "\nData: " + this.data + "\nHorário: " + this.horario +
                "\nTipo de evento: " + this.getTipoDeEvento();
    }
}
