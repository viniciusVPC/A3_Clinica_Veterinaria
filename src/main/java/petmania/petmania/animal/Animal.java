//Cuida da camada de classe
package petmania.petmania.animal;

import java.sql.ClientInfoStatus;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import jakarta.persistence.CascadeType;
//responsáveis por trabalhar em conjunto com o MySQL
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import petmania.petmania.cliente.Cliente;

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

    public int getIdade() {
        return Period.between(this.dataNasc, LocalDate.now()).getYears();
    }

    // constutor vazio
    public Animal() {

    }

    // constutor sem o Id
    public Animal(String nome, LocalDate dataNasc, String especie, String raca) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.especie = especie;
        this.raca = raca;
    }

    // construtor com todos os atributos
    public Animal(Long id, String nome, LocalDate dataNasc, String especie, String raca) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.especie = especie;
        this.raca = raca;
    }
}
