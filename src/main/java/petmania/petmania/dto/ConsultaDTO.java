package petmania.petmania.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaDTO {
    @NotEmpty(message = "Tipo de consulta é obrigatório")
    private String tipo;
    @NotEmpty(message = "Horário da consulta é obrigatório")
    @Future(message = "Horário de consulta inválido")
    private LocalDateTime horario;
    @NotEmpty(message = "Duração da consulta é obrigatória")
    @DecimalMin(value = "5", message = "Duração de consulta inválida")
    private Integer duracaoEmMinutos;
}
