package com.careconnect.model.entity;

import jakarta.persistence.*;

@Entity
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf; // salve sem pontuação

    // outros campos (nome, email, etc.)

    public Long getId() { return id; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    // getters/setters dos demais campos...
}
