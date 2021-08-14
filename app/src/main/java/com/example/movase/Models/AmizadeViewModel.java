package com.example.movase.Models;

public class AmizadeViewModel extends Usuario{
    private boolean amigo;
    private String idSolicitante;
    private String idUsuario;

    public boolean isAmigo() {
        return this.amigo;
    }

    public void setAmigo(boolean amigo) {
        this.amigo = amigo;
    }

    public String getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(String idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public AmizadeViewModel() {
    }

    public AmizadeViewModel(String nomeCompleto, String apelido, String dataNascimento, String celular, String esportePreferido, String endereco, String sexo, String sexoPrefConexao, String fotoPerfil) {
        super(nomeCompleto, apelido, dataNascimento, celular, esportePreferido, endereco, sexo, sexoPrefConexao, fotoPerfil);
    }
}
