package petmania.petmania.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import petmania.petmania.dto.IsAfter;

@Getter
@Setter
@ToString
@Entity
public class Administrador {
    // Atributos
    @Id // Indica que é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // Primary Key

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataNasc;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    // Construtores
    // Construtor vazio
    public Administrador() {
    }

    // Construtor sem o id
    public Administrador(String nome, LocalDate dataNasc, String cpf, String email) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
    }

    // Construtor com todos os atributos
    public Administrador(Long id, String nome, LocalDate dataNasc, String cpf, String email) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
    }

}
