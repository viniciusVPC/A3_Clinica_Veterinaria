// Cuida da camada API
package petmania.petmania.controller;

import jakarta.validation.Valid;
import petmania.petmania.model.Cliente;
import petmania.petmania.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/clientes")
public class ClienteController {

    @Autowired
    ClienteRepository repo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Cliente cliente) {
        return "/clientes/add-cliente";
    }

    @PostMapping("/addcliente")
    public String addCliente(@Valid Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/clientes/add-cliente";
        }

        repo.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping({ "", "/" })
    public String mostraListaClientes(Model model) {
        var clientes = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("clientes", clientes);
        return "/clientes/index";
    }

    @GetMapping("/edit/{id}")
    public String mostraFormularioUpdate(@PathVariable("id") Long id, Model model) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + id + " não existe."));
        model.addAttribute("cliente", cliente);
        return "/clientes/update-cliente";
    }

    @PostMapping("/update/{id}")
    public String updateCliente(@PathVariable("id") Long id, @Valid Cliente cliente, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "/clientes/update-cliente";
        }
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

}
