package com.careconnect.model.dto;

import com.careconnect.model.enums.UserRole;

public class SignupRequestDTO {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private UserRole tipo; // PACIENTE | ADMIN | CLINICA
    private String cpf;    // usado quando PACIENTE/ADMIN
    private String cnpj;   // usado quando CLINICA

    public SignupRequestDTO() {}

    // getters/setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public UserRole getTipo() { return tipo; }
    public void setTipo(UserRole tipo) { this.tipo = tipo; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}
