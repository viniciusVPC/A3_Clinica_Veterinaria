// Cuida da camada API
package petmania.petmania.cliente;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "api/v1/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // função GET da api
    @GetMapping
    public List<Cliente> getClientes() {
        return clienteService.getClientes();
    }

    // função POST da api
    @PostMapping
    // RequestBody mapeia automaticamente o objeto Animal do BODY do POST da api
    public void registraNovoCliente(@RequestBody Cliente cliente) {
        System.out.println(cliente);
        clienteService.addNewCliente(cliente);
    }

    // função DELETE da api
    @DeleteMapping(path = "{idCliente}")
    public void deleteCliente(@PathVariable("idCliente") Long idCliente) {
        // TODO verificar id not null
        clienteService.deleteCliente(idCliente);
    }

    // função PUT da api
    @PutMapping(path = "{idCliente}")
    public void updateCliente(@PathVariable("idCliente") Long idCliente,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String CPF,
            @RequestParam(required = false) String email) {
        clienteService.updateCliente(idCliente, nome, CPF, email);
    }
}
