package petmania.petmania.cliente;

//responsáveis por criar getters, setters e um construtor
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {
    //Atributos
    private Long id;
    private String nome;
    private String CPF;
    private Long idPedido;
}
