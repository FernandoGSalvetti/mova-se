package com.example.movase.Models;

public class UsuarioRegister extends Usuario {
    private String email;
    private String senha;

    public UsuarioRegister() {
    };

    public UsuarioRegister(String nomeCompleto, String apelido, String dataNascimento, String celular, String esportePreferido, String endereco, String sexo, String sexoPrefConexao, String email, String senha) {
        super(nomeCompleto, apelido, dataNascimento, celular, esportePreferido, endereco, sexo, sexoPrefConexao);
        this.email = email;
        this.senha = senha;
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    };

    @Override
    public String toString() {
        return "UsuarioRegister{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                "nome" + getNomeCompleto() +
                "Apelido" + getApelido() +
                "DataNasc" + getDataNascimento() +
                '}';
    }
}
