//Cuida da camada de classe
package petmania.petmania.animal;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
//responsáveis por trabalhar em conjunto com o MySQL
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import petmania.petmania.cliente.Cliente;
import petmania.petmania.consulta.Consulta;

@Getter
@Setter
@ToString
@Entity
public class Animal {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // primary key
    private String nome;
    private LocalDate dataNasc;
    // atributo idade não aparece na TABLE, é calculada automaticamente e exibida no
    // GET da api
    @Transient
    private int idade;
    private String especie;
    private String raca;

    @JsonIgnore
    @ManyToMany(mappedBy = "pets")
    private Set<Cliente> donos = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAnimal", referencedColumnName = "id")
    private Set<Consulta> consultas = new HashSet<>();

    // Funções
    public int getIdade() {
        return Period.between(this.dataNasc, LocalDate.now()).getYears();
    }

    // Construtores
    // constutor vazio
    public Animal() {

    }

    // constutor sem o Id
    public Animal(String nome, LocalDate dataNasc, int idade, String especie, String raca, Set<Cliente> donos,
            Set<Consulta> consultas) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.idade = idade;
        this.especie = especie;
        this.raca = raca;
        this.donos = donos;
        this.consultas = consultas;
    }

    // construtor com todos os atributos
    public Animal(Long id, String nome, LocalDate dataNasc, int idade, String especie, String raca, Set<Cliente> donos,
            Set<Consulta> consultas) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.idade = idade;
        this.especie = especie;
        this.raca = raca;
        this.donos = donos;
        this.consultas = consultas;
    }
}
