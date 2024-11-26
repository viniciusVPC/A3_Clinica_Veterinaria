package petmania.petmania.model;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    public Consulta(Cliente cliente, Animal animal, Doutor doutor, String tipo, LocalDateTime horario,
            Integer duracaoEmMinutos) {
        this.cliente = cliente;
        this.animal = animal;
        this.doutor = doutor;
        this.tipo = tipo;
        this.horario = horario;
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    public Consulta(Long idConsulta, Cliente cliente, Animal animal, Doutor doutor, String tipo, LocalDateTime horario,
            Integer duracaoEmMinutos) {
        this.idConsulta = idConsulta;
        this.cliente = cliente;
        this.animal = animal;
        this.doutor = doutor;
        this.tipo = tipo;
        this.horario = horario;
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

}
