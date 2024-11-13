package petmania.petmania.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import petmania.petmania.dto.IsAfter;

@Getter
@Setter
@ToString
@Entity
public class Cliente {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // Primary key

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotNull(message = "Data de nascimento é obrigatória.")
    @Past(message = "Data de nascimento inválida.")
    @IsAfter(current = "1900-01-01", message = "Data de nascimento inválida.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNasc;

    @NotBlank(message = "Cpf é obrigatório.")
    private String cpf;

    @NotBlank(message = "Email é obrigatório.")
    @Email(message = "Email inválido.")
    private String email;

    @ManyToMany
    @JoinTable(name = "cliente_animal", joinColumns = @JoinColumn(name = "idCliente"), inverseJoinColumns = @JoinColumn(name = "idAnimal"))
    private Set<Animal> pets = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCliente", referencedColumnName = "id")
    private Set<Consulta> consultas = new HashSet<>();

    // Construtores
    public Cliente() {

    }

    // Construtor sem o id
    public Cliente(String nome, LocalDate dataNasc, String cpf, String email, Set<Animal> pets,
            Set<Consulta> consultas) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
        this.pets = pets;
        this.consultas = consultas;
    }

    // Construtor com todos os atributos
    public Cliente(Long id, String nome, LocalDate dataNasc, String cpf, String email, Set<Animal> pets,
            Set<Consulta> consultas) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
        this.pets = pets;
        this.consultas = consultas;
    }
}
