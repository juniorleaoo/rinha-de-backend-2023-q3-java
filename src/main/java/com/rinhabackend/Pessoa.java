package com.rinhabackend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    private UUID id;
    @Column(name = "apelido", nullable = false, length = 32, unique = true)
    private String apelido;
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    @Column(name = "nascimento", nullable = false)
    private String nascimento;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stack", columnDefinition = "jsonb", nullable = false)
    private List<String> stack;

    public Pessoa() {

    }

    public Pessoa(UUID id, String apelido, String nome, String nascimento, List<String> stack) {
        this.id = id;
        this.apelido = apelido;
        this.nome = nome;
        this.nascimento = nascimento;
        this.stack = stack;
    }

    public Pessoa(String id, String apelido, String nome, String nascimento, List<String> stack) {
        this.id = UUID.fromString(id);
        this.apelido = apelido;
        this.nome = nome;
        this.nascimento = nascimento;
        this.stack = stack;
    }

    public boolean valido() {
        var apelidoValido = apelido != null && !apelido.isEmpty() && apelido.length() < 32;
        var nomeValido = nome != null && !nome.isEmpty() && nome.length() < 100;
        var nascimentoValido = nascimento != null;
        var stackValida = stack != null && stack.stream().allMatch(value -> value != null && value.length() < 32);
        return apelidoValido && nomeValido && nascimentoValido && stackValida;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }
}
