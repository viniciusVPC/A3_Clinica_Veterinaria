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

    @Autowired
    private AuthenticationController authController;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Model model) {
        AdministradorDTO adminDTO = new AdministradorDTO();
        model.addAttribute("administradorDto", adminDTO);
        return "/admins/add-admin";
    }

    // ADD ADMIN é Controlado pela classe AuthenticationController
    /*
     * @PostMapping("/addadmin")
     * public String addAdmin(@Valid @ModelAttribute("administradorDto")
     * AdministradorDTO adminDto, BindingResult result,
     * Model model) {
     * // TODO CPF e senha únicos
     * if (result.hasErrors()) {
     * return "/admins/add-admin";
     * }
     * model.addAttribute("administradorDto", adminDTO);
     * return "/auth/register";
     * }
     */

    @GetMapping({ "", "/" })
    public String mostraListaAdmins(Model model) {
        var admins = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("admins", admins);
        return "/admins/index";
    }

    /*
     * @GetMapping("/edit")
     * public String mostraFormularioUpdate(@RequestParam Long id, Model model) {
     * Administrador admin = repo.findById(id)
     * .orElseThrow(() -> new IllegalStateException("Administrador com id " + id +
     * " não existe."));
     * AdministradorDTO adminDto = new AdministradorDTO(admin.getNome(),
     * admin.getDataNasc(), admin.getCpf(),
     * admin.getEmail(), admin.getSenha());
     * model.addAttribute("admin", admin);
     * model.addAttribute("administradorDto", adminDto);
     * return "/admins/update-admin";
     * }
     */

    /*
     * @PostMapping("/edit")
     * public String updateAdmin(@RequestParam Long id,
     * 
     * @Valid @ModelAttribute("AdministradorDto") AdministradorDTO adminDto,
     * BindingResult result,
     * Model model) {
     * Administrador admin = repo.findById(id)
     * .orElseThrow(() -> new IllegalStateException("Administrador com id " + id +
     * " não existe."));
     * if (result.hasErrors()) {
     * return "/admins/update-admin";
     * }
     * 
     * admin = new Administrador(adminDto.getNome(), adminDto.getDataNasc(),
     * adminDto.getCpf(),
     * adminDto.getEmail(), adminDto.getSenha());
     * admin.setId(id);
     * repo.save(admin);
     * return "redirect:/admins";
     * }
     */

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id, Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));
        repo.delete(admin);
        return "redirect:/admins";
    }

}
