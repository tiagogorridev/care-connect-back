package com.careconnect.model.dto;

public class SigninRequestDTO {

    private String email;
    private String senha;

    // Construtor vazio (necessário para o Spring desserializar JSON)
    public SigninRequestDTO() {}

    // Construtor com argumentos (opcional, mas útil)
    public SigninRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
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
    }
}
