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
import jakarta.persistence.OneToMany;
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
public class Doutor {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long idDoutor;

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

    @NotBlank(message = "Especialidade é obrigatória.")
    private String especialidade;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idDoutor", referencedColumnName = "idDoutor")
    private Set<Consulta> consultas = new HashSet<>();

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
