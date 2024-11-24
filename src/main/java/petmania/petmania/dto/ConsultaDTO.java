package petmania.petmania.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    @NotEmpty(message = "CPF do cliente é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpfCliente;

    @NotEmpty(message = "Nome do pet é obrigatório")
    private String nomeAnimal;

    @NotEmpty(message = "CPF do doutor é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpfDoutor;

    @NotEmpty(message = "Tipo de consulta é obrigatório")
    private String tipo;

    @NotNull(message = "Horário da consulta é obrigatório")
    @Future(message = "Horário de consulta inválido")
    private LocalDateTime horario;

    @NotNull(message = "Duração da consulta é obrigatória")
    @DecimalMin(value = "5", message = "Duração de consulta inválida")
    private int duracaoEmMinutos;

    public ConsultaDTO(String cpfCliente, String cpfDoutor) {
        this.cpfCliente = cpfCliente;
        this.cpfDoutor = cpfDoutor;
    }
}
