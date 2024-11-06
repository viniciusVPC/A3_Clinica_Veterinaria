package petmania.petmania.model;

import java.time.LocalDateTime;

/* import jakarta.persistence.CascadeType; */
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
/* import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne; */
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

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idAnimal")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "idDoutor")
    private Doutor doutor;

    private String tipo;
    private LocalDateTime horario;
    private Integer duracaoEmMinutos;

    // Construtores
    // Construtor vazio
    public Consulta() {
    }

    // Construtor sem o id
    public Consulta(String tipo, LocalDateTime horario, Integer duracaoEmMinutos) {
        this.tipo = tipo;
        this.horario = horario;
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    // Construtor com todos os atributos
    public Consulta(Long idConsulta, Long idAnimal, Long idCliente, String tipo, LocalDateTime horario,
            Integer duracaoEmMinutos) {
        this.idConsulta = idConsulta;
        this.tipo = tipo;
        this.horario = horario;
        this.duracaoEmMinutos = duracaoEmMinutos;
    }
}
