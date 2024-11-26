// Cuida da camada API
package petmania.petmania.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import petmania.petmania.dto.ClienteDTO;
import petmania.petmania.model.Cliente;
import petmania.petmania.repository.ClienteRepository;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Model model) {
        ClienteDTO clienteDTO = new ClienteDTO();
        model.addAttribute("clienteDto", clienteDTO);
        return "/clientes/add-cliente";
    }

    @PostMapping("/addcliente")
    public String addCliente(@Valid @ModelAttribute("clienteDto") ClienteDTO clienteDto, BindingResult result,
            Model model) {

        boolean error = false;

        if (result.hasErrors()) {
            return "/clientes/add-cliente";
        }

        Optional<Cliente> clienteOptional = repo.findClienteByEmail(clienteDto.getEmail());
        if (clienteOptional.isPresent()) {
            model.addAttribute("errorEmail", "Já existe um cliente com esse email!");
            error = true;
        }

        clienteOptional = repo.findClienteByCpf(clienteDto.getCpf());
        if (clienteOptional.isPresent()) {
            model.addAttribute("errorCPF", "Já existe um cliente com esse CPF!");
            error = true;
        }

        if (error)
            return "/clientes/add-cliente";

        Cliente cliente = new Cliente(clienteDto.getNome(), clienteDto.getDataNasc(), clienteDto.getCpf(),
                clienteDto.getEmail(), null, null);

        repo.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping({ "", "/" })
    public String mostraListaClientes(Model model) {
        var clientes = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        if (clientes.isEmpty())
            model.addAttribute("clientes", null);
        else
            model.addAttribute("clientes", clientes);
        return "/clientes/index";
    }

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long id, Model model) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + id + " não existe."));
        ClienteDTO clienteDto = new ClienteDTO(cliente.getNome(), cliente.getDataNasc(), cliente.getCpf(),
                cliente.getEmail(), null, null);
        model.addAttribute("cliente", cliente);
        model.addAttribute("clienteDto", clienteDto);
        return "/clientes/update-cliente";
    }

    @PostMapping("/edit")
    public String updateCliente(@RequestParam Long id, @Valid @ModelAttribute("clienteDto") ClienteDTO clienteDto,
            BindingResult result, Model model) {
        boolean error = false;

        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + id + " não existe."));

        if (result.hasErrors()) {
            return "/clientes/update-cliente";
        }

        Optional<Cliente> clienteOptional = repo.findClienteByEmail(clienteDto.getEmail());
        if (clienteOptional.isPresent()) {
            if (!clienteOptional.get().equals(cliente)) {
                model.addAttribute("errorEmail", "Já existe um cliente com esse email!");
                error = true;
            }
        }

        clienteOptional = repo.findClienteByCpf(clienteDto.getCpf());
        if (clienteOptional.isPresent()) {
            if (!clienteOptional.get().equals(cliente)) {
                model.addAttribute("errorCPF", "Já existe um cliente com esse CPF!");
                error = true;
            }
        }

        if (error)
            return "/clientes/add-cliente";

        cliente = new Cliente(clienteDto.getNome(), clienteDto.getDataNasc(), clienteDto.getCpf(),
                clienteDto.getEmail(), null, null);
        cliente.setId(id);
        repo.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/delete/{id}")
    public String deleteCliente(@PathVariable("id") Long id, Model model) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + id + " não existe."));
        repo.delete(cliente);
        return "redirect:/clientes";
    }

    /// TODO conectar animal ao dono
    /// Adiciona animal ao dono usando o CPF do dono (nova coluna na tabela
    /// cliente_animal)
    /// Procura animais por clientes: procura na tabela cliente_animal pelos IDs dos
    /// animais
    /// procura na tabela animais os dados dos animais com respectivos IDs

}
