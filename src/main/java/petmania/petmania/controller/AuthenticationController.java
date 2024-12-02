package petmania.petmania.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import petmania.petmania.dto.AdministradorDTO;
import petmania.petmania.dto.AuthenticationDTO;
import petmania.petmania.dto.LoginResponseDTO;
import petmania.petmania.infra.security.TokenService;
import petmania.petmania.model.Administrador;
import petmania.petmania.model.UserRole;
import petmania.petmania.repository.AdministradorRepository;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AdministradorRepository repo;
    @Autowired
    TokenService tokenService;

    @GetMapping("/login")
    public String mostraFormularioLogin(Model model) {
        AuthenticationDTO authenticationDto = new AuthenticationDTO();
        model.addAttribute("authenticationDto", authenticationDto);

        return "/admins/login";
    }

    @GetMapping("/login-error")
    public String mostraFormularioLoginErro(Model model) {
        AuthenticationDTO authenticationDto = new AuthenticationDTO();
        model.addAttribute("authenticationDto", authenticationDto);
        model.addAttribute("loginError", true);
        return "/admins/login";
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDto) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDto.getLogin(),
                authenticationDto.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Administrador) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
        // return "/home";
    }

    @GetMapping("/register")
    public String mostraFormularioSignin(Model model) {
        AdministradorDTO adminDTO = new AdministradorDTO();
        model.addAttribute("adminDto", adminDTO);
        return "/admins/register";
    }

    @GetMapping("/addadmin")
    public String mostraFormularioAddAdmin(Model model) {
        AdministradorDTO adminDTO = new AdministradorDTO();
        model.addAttribute("adminDto", adminDTO);
        return "/admins/add-admin";
    }

    @PostMapping("/addadmin")
    public String addAdmin(@Valid @ModelAttribute("adminDto") AdministradorDTO administradorDto,
            BindingResult result, Model model) {
        boolean error = false;

        if (result.hasErrors()) {
            return "/admins/add-admin";
        }

        if (!administradorDto.getDataNasc().isBefore(LocalDate.now().minusYears(18))) {
            model.addAttribute("errorIdade", "Administradores precisam ser maiores de idade!");
            error = true;
        }

        Optional<Administrador> adminOptional = repo.findAdminByEmail(administradorDto.getEmail());
        if (adminOptional.isPresent()) {
            model.addAttribute("errorEmail", "Já existe um administrador com esse email!");
            error = true;
        }

        adminOptional = repo.findAdminByCpf(administradorDto.getCpf());
        if (adminOptional.isPresent()) {
            model.addAttribute("errorCPF", "Já existe um administrador com esse CPF!");
            error = true;
        }

        if (error)
            return "/admins/add-admin";

        String encryptedPassword = new BCryptPasswordEncoder().encode(administradorDto.getSenha());
        Administrador admin = new Administrador(administradorDto.getNome(), administradorDto.getDataNasc(),
                administradorDto.getCpf(), administradorDto.getEmail(), encryptedPassword,
                UserRole.ADMIN);

        repo.save(admin);

        return "redirect:/admins";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("adminDto") AdministradorDTO administradorDto,
            BindingResult result, Model model) {
        boolean error = false;

        if (result.hasErrors()) {
            return "/admins/register";
        }

        if (!administradorDto.getDataNasc().isBefore(LocalDate.now().minusYears(18))) {
            model.addAttribute("errorIdade", "Administradores precisam ser maiores de idade!");
            error = true;
        }

        Optional<Administrador> adminOptional = repo.findAdminByEmail(administradorDto.getEmail());
        if (adminOptional.isPresent()) {
            model.addAttribute("errorEmail", "Já existe um administrador com esse email!");
            error = true;
        }

        adminOptional = repo.findAdminByCpf(administradorDto.getCpf());
        if (adminOptional.isPresent()) {
            model.addAttribute("errorCPF", "Já existe um administrador com esse CPF!");
            error = true;
        }

        if (error)
            return "/admins/register";

        String encryptedPassword = new BCryptPasswordEncoder().encode(administradorDto.getSenha());
        Administrador admin = new Administrador(administradorDto.getNome(), administradorDto.getDataNasc(),
                administradorDto.getCpf(), administradorDto.getEmail(), encryptedPassword,
                UserRole.ADMIN);

        repo.save(admin);

        return "redirect:/home";
    }

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long id, Model model) {
        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));
        AdministradorDTO adminDto = new AdministradorDTO(admin.getNome(), admin.getDataNasc(), admin.getCpf(),
                admin.getEmail(), admin.getSenha());
        model.addAttribute("admin", admin);
        model.addAttribute("administradorDto", adminDto);
        return "/admins/update-admin";
    }

    @PostMapping("/edit")
    public String updateAdmin(@RequestParam Long id,
            @Valid @ModelAttribute("administradorDto") AdministradorDTO adminDto, BindingResult result,
            Model model) {
        boolean error = false;

        Administrador admin = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Administrador com id " + id + " não existe."));

        if (result.hasErrors()) {
            return "/admins/update-admin";
        }

        if (!adminDto.getDataNasc().isBefore(LocalDate.now().minusYears(18))) {
            model.addAttribute("errorIdade", "Administradores precisam ser maiores de idade!");
            error = true;
        }

        Optional<Administrador> adminOptional = repo.findAdminByEmail(adminDto.getEmail());
        if (adminOptional.isPresent()) {
            if (adminOptional.get().equals(admin)) {
                System.out.println("É esse usuário que eu encontrei.");
            } else {
                System.out.println("É outro usuário com o mesmo email");
                model.addAttribute("errorEmail", "Já existe um administrador com esse email!");
                error = true;
            }

        }

        adminOptional = repo.findAdminByCpf(adminDto.getCpf());
        if (adminOptional.isPresent()) {
            if (adminOptional.get().equals(admin)) {
                System.out.println("É esse usuário que eu encontrei.");
            } else {
                System.out.println("É outro usuário com o mesmo cpf");
                model.addAttribute("errorCPF", "Já existe um administrador com esse CPF!");
                error = true;
            }
        }

        if (error)
            return "/admins/update-admin";

        String encryptedPassword = new BCryptPasswordEncoder().encode(adminDto.getSenha());
        admin = new Administrador(adminDto.getNome(), adminDto.getDataNasc(), adminDto.getCpf(), adminDto.getEmail(),
                encryptedPassword,
                UserRole.ADMIN);
        admin.setId(id);
        repo.save(admin);
        return "redirect:/admins";
    }
}
