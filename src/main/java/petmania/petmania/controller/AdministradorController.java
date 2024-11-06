package petmania.petmania.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import petmania.petmania.model.Administrador;
import petmania.petmania.service.AdministradorService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "api/v1/admin")
public class AdministradorController {

    @Autowired
    private AdministradorService adminService;

    public AdministradorController(AdministradorService adminService) {
        this.adminService = adminService;
    }

    // Função GET da api
    @GetMapping
    public List<Administrador> getAdmins() {
        return adminService.getAdmins();
    }

    // Função POST da api
    @PostMapping
    public void registraNovoAdmin(@RequestBody Administrador admin) {
        adminService.addNewAdmin(admin);
    }

    // Função DELETE da api
    @DeleteMapping(path = "{idAdmin}")
    public void deleteAdmin(@PathVariable("idAdmin") Long idAdmin) {
        adminService.deleteAdmin(idAdmin);
    }

    // Função PUT da api
    @PutMapping(path = "{idAdmin}")
    public void updateAdmin(@PathVariable("idAdmin") Long idAdmin,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) LocalDate dataNasc,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email) {
        adminService.updateAdmin(idAdmin, nome, dataNasc, cpf, email);
    }

}
