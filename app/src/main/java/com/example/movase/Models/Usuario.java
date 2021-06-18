package com.example.movase.Models;

import java.util.Date;

public abstract class Usuario {
    private String nomeCompleto;
    private String apelido;
    private String dataNascimento;
    private String celular;
    private String esportePreferido;
    private String endereco;
    private String sexo;
    private String sexoPrefConexao;
    public Usuario(){

    }
    public Usuario(String nomeCompleto, String apelido, String dataNascimento, String celular, String esportePreferido, String endereco, String sexo, String sexoPrefConexao) {
        this.nomeCompleto = nomeCompleto;
        this.apelido = apelido;
        this.dataNascimento = dataNascimento;
        this.celular = celular;
        this.esportePreferido = esportePreferido;
        this.endereco = endereco;
        this.sexo = sexo;
        this.sexoPrefConexao = sexoPrefConexao;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
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
        return dataNascimento;
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
        return esportePreferido;
    }

    public void setEsportePreferido(String esportePreferido) {
        this.esportePreferido = esportePreferido;
    }

    public String getEndereço() {
        return endereco;
    }

    public void setEndereço(String endereço) {
        this.endereco = endereço;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSexoPrefConexao() {
        return sexoPrefConexao;
    }

    public void setSexoPrefConexao(String sexoPrefConexao) {
        this.sexoPrefConexao = sexoPrefConexao;
    }


}
