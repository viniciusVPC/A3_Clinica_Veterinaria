package petmania.petmania.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorDTO {
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

    @NotBlank(message = "Senha é obrigatória.")
    private String senha;
}
