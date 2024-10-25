package petmania.petmania.pedido;

import java.time.LocalDate;

//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pedido {
    //Atributos
    private Long id;
    private Long idAnimal;
    private Long idCliente;
    private String tipo;
    private LocalDate data;
}
