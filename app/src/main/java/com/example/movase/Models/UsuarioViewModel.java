package com.example.movase.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class UsuarioViewModel implements Parcelable {
    private String id;
    private String nomeCompleto;
    private String apelido;
    private String dataNascimento;
    private String celular;
    private String esportePreferido;
    private String endereco;
    private String sexo;
    private String sexoPrefConexao;
    private String fotoPerfil = "";
    public UsuarioViewModel(){

    }

    public UsuarioViewModel(String nomeCompleto, String apelido, String dataNascimento, String celular, String esportePreferido, String endereco, String sexo, String sexoPrefConexao, String fotoPerfil, String id) {
        this.nomeCompleto = nomeCompleto;
        this.apelido = apelido;
        this.dataNascimento = dataNascimento;
        this.celular = celular;
        this.esportePreferido = esportePreferido;
        this.endereco = endereco;
        this.sexo = sexo;
        this.sexoPrefConexao = sexoPrefConexao;
        this.fotoPerfil = fotoPerfil;
        this.id = id;
    }

    protected UsuarioViewModel(Parcel in) {
        nomeCompleto = in.readString();
        apelido = in.readString();
        dataNascimento = in.readString();
        celular = in.readString();
        esportePreferido = in.readString();
        endereco = in.readString();
        sexo = in.readString();
        sexoPrefConexao = in.readString();
        fotoPerfil = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nomeCompleto);
        dest.writeString(apelido);
        dest.writeString(dataNascimento);
        dest.writeString(celular);
        dest.writeString(esportePreferido);
        dest.writeString(endereco);
        dest.writeString(sexo);
        dest.writeString(sexoPrefConexao);
        dest.writeString(fotoPerfil);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UsuarioViewModel> CREATOR = new Creator<UsuarioViewModel>() {
        @Override
        public UsuarioViewModel createFromParcel(Parcel in) {
            return new UsuarioViewModel(in);
        }

        @Override
        public UsuarioViewModel[] newArray(int size) {
            return new UsuarioViewModel[size];
        }
    };
    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNomeCompleto() {
        return this.nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEsportePreferido() {
        return this.esportePreferido;
    }

    public void setEsportePreferido(String esportePreferido) {
        this.esportePreferido = esportePreferido;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSexo() {
        return this.sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSexoPrefConexao() {
        return this.sexoPrefConexao;
    }

    public void setSexoPrefConexao(String sexoPrefConexao) {
        this.sexoPrefConexao = sexoPrefConexao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UsuarioViewModel{" +
                "nomeCompleto='" + nomeCompleto + '\'' +
                ", apelido='" + apelido + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", celular='" + celular + '\'' +
                ", esportePreferido='" + esportePreferido + '\'' +
                ", endereco='" + endereco + '\'' +
                ", sexo='" + sexo + '\'' +
                ", sexoPrefConexao='" + sexoPrefConexao + '\'' +
                '}';
    }
}
