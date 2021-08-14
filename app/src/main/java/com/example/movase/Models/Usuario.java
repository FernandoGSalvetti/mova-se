package com.example.movase.Models;

import java.util.Date;
import java.util.List;

public abstract class Usuario {
    private String nomeCompleto;
    private String apelido;
    private String dataNascimento;
    private String celular;
    private String esportePreferido;
    private String endereco;
    private String sexo;
    //private List<Evento>;
    private String sexoPrefConexao;
    private String fotoPerfil = "";
    public Usuario(){

    }

    public Usuario(String nomeCompleto, String apelido, String dataNascimento, String celular, String esportePreferido, String endereco, String sexo, String sexoPrefConexao, String fotoPerfil) {
        this.nomeCompleto = nomeCompleto;
        this.apelido = apelido;
        this.dataNascimento = dataNascimento;
        this.celular = celular;
        this.esportePreferido = esportePreferido;
        this.endereco = endereco;
        this.sexo = sexo;
        this.sexoPrefConexao = sexoPrefConexao;
        this.fotoPerfil = fotoPerfil;
    }

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
        return endereco;
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


}
