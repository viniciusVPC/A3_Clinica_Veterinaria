package petmania.petmania.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String mostraFormularioSignUp(Model model) {
        AdministradorDTO adminDTO = new AdministradorDTO();
        model.addAttribute("administradorDto", adminDTO);
        return "/admins/add-admin";
    }

    @PostMapping("/addadmin")
    public String addAdmin(@Valid @ModelAttribute("administradorDto") AdministradorDTO adminDto, BindingResult result,
            Model model) {

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

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long id, Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));
        AdministradorDTO adminDto = new AdministradorDTO(admin.getNome(), admin.getDataNasc(), admin.getCpf(),
                admin.getEmail());
        model.addAttribute("admin", admin);
        model.addAttribute("administradorDto", adminDto);
        return "/admins/update-admin";
    }

    @PostMapping("/edit")
    public String updateAdmin(@RequestParam Long id,
            @Valid @ModelAttribute("AdministradorDto") AdministradorDTO adminDto, BindingResult result,
            Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + id + " não existe."));
        if (result.hasErrors()) {
            return "/admins/update-admin";
        }

        admin = new Administrador(adminDto.getNome(), adminDto.getDataNasc(), adminDto.getCpf(),
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
