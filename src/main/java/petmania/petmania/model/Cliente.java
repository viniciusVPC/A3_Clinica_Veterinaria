package petmania.petmania.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Cliente {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // Primary key

    // @NotBlank(message = "Nome é obrigatório.")
    @Column(nullable = false)
    private String nome;

    // @NotNull(message = "Data de nascimento é obrigatória.")
    // @Past(message = "Data de nascimento inválida.")
    // @IsAfter(current = "1900-01-01", message = "Data de nascimento inválida.")
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataNasc;

    // @NotBlank(message = "Cpf é obrigatório.")
    @Column(unique = true, nullable = false)
    private String cpf;

    // @NotBlank(message = "Email é obrigatório.")
    // @Email(message = "Email inválido.")
    @Column(unique = true, nullable = false)
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
