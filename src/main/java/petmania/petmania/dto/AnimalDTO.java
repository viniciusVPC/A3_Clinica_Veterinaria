package petmania.petmania.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalDTO {

    @NotEmpty(message = "Nome é obrigatório")
    private String nome;
    @NotEmpty(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento inválida")
    private LocalDate dataNasc;
    @NotEmpty(message = "Espécie é obrigatória")
    String especie;
    @NotEmpty(message = "Raça é obrigatória")
    String raca;
}
