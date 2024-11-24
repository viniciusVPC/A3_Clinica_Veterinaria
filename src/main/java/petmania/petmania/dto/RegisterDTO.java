package petmania.petmania.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "Email é obrigatório.")
    private String login;
    @NotBlank(message = "Senha é obrigatória.")
    private String password;
    @NotBlank(message = "Papel é obrigatório.")
    private String role;
}
