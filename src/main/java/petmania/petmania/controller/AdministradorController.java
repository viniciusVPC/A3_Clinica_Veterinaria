package petmania.petmania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import petmania.petmania.dto.AdministradorDTO;
import petmania.petmania.model.Administrador;
import petmania.petmania.repository.AdministradorRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/admins")
public class AdministradorController {

    @Autowired
    private AdministradorRepository repo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Administrador admin) {
        return "/admins/add-admin";
    }

    @PostMapping("/addadmin")
    public String addAdmin(@Valid AdministradorDTO adminDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/admins/add-admin";
        }
        Administrador admin = new Administrador(adminDto.getNome(), adminDto.getDataNasc(), adminDto.getCpf(),
                adminDto.getEmail());

        repo.save(admin);
        return "redirect:/admins";
    }

    @GetMapping({ "", "/" })
    public String mostraListaAdmins(Model model) {
        var admins = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("admins", admins);
        return "/admins/index";
    }

    @GetMapping("/edit/{id}")
    public String mostraFormularioUpdate(@PathVariable("id") Long id, Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));
        model.addAttribute("admin", admin);
        return "/admins/update-admin";
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable("id") Long id, @Valid AdministradorDTO adminDto, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "/admins/update-admin";
        }
        Administrador admin = new Administrador(adminDto.getNome(), adminDto.getDataNasc(), adminDto.getCpf(),
                adminDto.getEmail());
        admin.setId(id);
        repo.save(admin);
        return "redirect:/admins";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id, Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));
        repo.delete(admin);
        return "redirect:/admins";
    }

}
