package petmania.petmania.consulta;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Consulta {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long idConsulta; // Primary Key

    private Long idAnimal;
    private Long idCliente;
    private String tipo;
    private LocalDateTime horario;

    // Construtores
    // Construtor vazio
    public Consulta() {
    }

    // Construtor sem o id
    public Consulta(Long idAnimal, Long idCliente, String tipo, LocalDateTime horario) {
        this.idAnimal = idAnimal;
        this.idCliente = idCliente;
        this.tipo = tipo;
        this.horario = horario;
    }

    // Construtor com todos os atributos
    public Consulta(Long idConsulta, Long idAnimal, Long idCliente, String tipo, LocalDateTime horario) {
        this.idConsulta = idConsulta;
        this.idAnimal = idAnimal;
        this.idCliente = idCliente;
        this.tipo = tipo;
        this.horario = horario;
    }
}
