package petmania.petmania.dto;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Data de nascimento é obrigatória.")
    @Past(message = "Data de nascimento inválida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNasc;

    @NotBlank(message = "Espécie é obrigatória")
    String especie;

    @NotBlank(message = "Raça é obrigatória")
    String raca;

    // Funções
    public int getIdade() {
        return Period.between(this.dataNasc, LocalDate.now()).getYears();
    }

}
