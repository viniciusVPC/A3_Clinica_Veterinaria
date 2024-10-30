package petmania.petmania.doutor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import petmania.petmania.consulta.Consulta;

@Getter
@Setter
@ToString
@Entity
public class Doutor {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long idDoutor;
    private String nome;
    private LocalDate dataNasc;
    private String cpf;
    private String email;
    private String especialidade;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idDoutor", referencedColumnName = "idDoutor")
    private Set<Consulta> consultas = new HashSet<>();

    //TODO inserir atributo que mostra os horários que o doutor está ocupado?

    // Construtores
    // Construtor vazio
    public Doutor() {
    }

    // Construtor sem o id
    public Doutor(String nome, LocalDate dataNasc, String cpf, String email, String especialidade,
            Set<Consulta> consultas) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
        this.especialidade = especialidade;
        this.consultas = consultas;
    }

    // Construtor com todos os atributos
    public Doutor(Long idDoutor, String nome, LocalDate dataNasc, String cpf, String email, String especialidade,
            Set<Consulta> consultas) {
        this.idDoutor = idDoutor;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
        this.especialidade = especialidade;
        this.consultas = consultas;
    }

}
