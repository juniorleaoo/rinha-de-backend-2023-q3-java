package com.rinhabackend;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 2765821260250568277L;

    @Id
    private UUID id;
    @Column(name = "apelido", nullable = false, length = 32, unique = true)
    private String apelido;
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    @Column(name = "nascimento", nullable = false)
    private String nascimento;

    @Convert(converter = StringListConverter.class)
    @Column(name = "stack", columnDefinition = "text", nullable = false)
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
        var nascimentoValido = nascimento != null && isValidDate(nascimento);
        var stackValida = stack != null && stack.stream().allMatch(value -> value != null && value.length() < 32);
        return apelidoValido && nomeValido && nascimentoValido && stackValida;
    }

    private boolean isValidDate(String dateAsString) {
        try {
            DateTimeFormatter.ofPattern("yyyy[-MM[-dd]]").parseBest(dateAsString, LocalDate::from, YearMonth::from, Year::from);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pessoa pessoa = (Pessoa) o;

        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
