package petmania.petmania.cliente;

import java.time.LocalDate;

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
public class Cliente {
    // Atributos
    @Id // indica que este atributo é uma PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // número sequancial 1, 2, 3...
    private Long id; // Primary key
    private String nome;
    private LocalDate dataNasc;
    private String CPF;
    private String email;
    private Long idPedido; // Foreign key

    public Cliente() {

    }

    // Construtor sem o id
    public Cliente(String nome, LocalDate dataNasc, String CPF, String email, Long idPedido) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.CPF = CPF;
        this.email = email;
        this.idPedido = idPedido;
    }

    // Construtor com todos os atributos
    public Cliente(Long id, String nome, LocalDate dataNasc, String CPF, String email, Long idPedido) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.CPF = CPF;
        this.email = email;
        this.idPedido = idPedido;
    }

}
