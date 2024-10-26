//Cuida da camada de classe
package petmania.petmania.animal;

import java.time.LocalDate;
import java.time.Period;

//responsáveis por trabalhar em conjunto com o MySQL
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @Transient
    private int idade;
    private String especie;
    private String raca;
    private Long idDono; // foreign key

    public int getIdade() {
        return Period.between(this.dataNasc, LocalDate.now()).getYears();
    }

    // constutor vazio
    public Animal() {

    }

    // constutor sem o Id
    public Animal(String nome, LocalDate dataNasc, String especie, String raca, Long idDono) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.especie = especie;
        this.raca = raca;
        this.idDono = idDono;
    }

    // construtor com todos os atributos
    public Animal(Long id, String nome, LocalDate dataNasc, String especie, String raca, Long idDono) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.especie = especie;
        this.raca = raca;
        this.idDono = idDono;
    }
}
