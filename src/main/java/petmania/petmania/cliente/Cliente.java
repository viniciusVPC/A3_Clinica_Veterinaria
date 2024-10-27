package petmania.petmania.cliente;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import petmania.petmania.animal.Animal;

@Getter
@Setter
@ToString
@Entity
public class Cliente {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // Primary key
    private String nome;
    private LocalDate dataNasc;
    private String CPF;
    private String email;

    public Cliente() {

    }

    // Construtor sem o id
    public Cliente(String nome, LocalDate dataNasc, String CPF, String email) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.CPF = CPF;
        this.email = email;
    }

    // Construtor com todos os atributos
    public Cliente(Long id, String nome, LocalDate dataNasc, String CPF, String email) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.CPF = CPF;
        this.email = email;
    }

}
