package petmania.petmania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import petmania.petmania.dto.AdministradorDTO;
import petmania.petmania.model.Administrador;
import petmania.petmania.repository.AdministradorRepository;

@Controller
@RequestMapping("/admins")
public class AdministradorController {

    @Autowired
    private AdministradorRepository repo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Model model) {
        AdministradorDTO adminDTO = new AdministradorDTO();
        model.addAttribute("administradorDto", adminDTO);
        return "/admins/add-admin";
    }

    // ADD ADMIN é Controlado pela classe AuthenticationController

    @GetMapping({ "", "/" })
    public String mostraListaAdmins(Model model) {
        var admins = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("admins", admins);
        return "/admins/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id, Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));
        repo.delete(admin);
        return "redirect:/admins";
    }

}
