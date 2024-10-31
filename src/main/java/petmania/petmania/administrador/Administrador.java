package petmania.petmania.administrador;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Administrador {
    // Atributos
    @Id // Indica que é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // Primary Key
    private String nome;
    private LocalDate dataNasc;
    private String cpf;
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
