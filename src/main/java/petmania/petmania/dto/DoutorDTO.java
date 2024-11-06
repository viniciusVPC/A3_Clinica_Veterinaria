package petmania.petmania.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoutorDTO {
    @NotEmpty(message = "Nome é obrigatório")
    private String nome;
    @NotEmpty(message = "Data de nascimento é obrigatória")
    @IsAfter(current = "1900-01-01", message = "Data de nascimento inválida")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dataNasc;
    @NotEmpty(message = "cpf é obrigatório")
    private String cpf;
    @NotEmpty(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotEmpty(message = "Especialidade obrigatória")
    private String especialidade;
}
