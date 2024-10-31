package petmania.petmania.administrador;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AdministradorService {
    private AdministradorRepository adminRepository;

    public AdministradorService(AdministradorRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Administrador> getAdmins() {
        return adminRepository.findAll();
    }

    // Regras de negócio relacionadas ao cadastro de um novo Administrador
    // Verifica se já existe admin com mesmo cpf ou email
    public void addNewAdmin(Administrador admin) {
        Optional<Administrador> adminOptional = adminRepository.findAdminByCpf(admin.getCpf());
        if (adminOptional.isPresent()) {
            throw new IllegalStateException("Já existe um administrador com esse cpf");
        }
        adminOptional = adminRepository.findAdminByEmail(admin.getEmail());
        if (adminOptional.isPresent()) {
            throw new IllegalStateException("Já existe um administrador com esse email");
        }
        if (!admin.getDataNasc().isBefore(LocalDate.now().minusYears(18))) {
            throw new IllegalStateException(
                    "Data de nascimento inválida. Um administrador precisa ser maior de idade.");
        }
        adminRepository.save(admin);
    }

    // Regras de negócio relacionadas à remoção de um admin do BD
    // Verifica se existe admin com esse id. Se não, joga um erro
    public void deleteAdmin(Long idAdmin) {
        boolean existe = adminRepository.existsById(idAdmin);
        if (!existe) {
            throw new IllegalStateException("Administrador com id " + idAdmin + " não existe.");
        }
        adminRepository.deleteById(idAdmin);
    }

    // Regras de negócio relacionadas à edição de um administrador já existente.
    // Verifica se existe admin com esse id. Se não, joga um erro.
    // Verifica se nova idade é maior que 18 anos
    // Verifica se já existe um admin com mesmo cpf ou email
    @Transactional
    public void updateAdmin(Long idAdmin, String nome, LocalDate dataNasc, String cpf, String email) {
        Administrador admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new IllegalStateException("Administrador com id: " + idAdmin + " não existe."));
        if (nome != null && nome.length() > 0 && !Objects.equals(admin.getNome(), nome)) {
            admin.setNome(nome);
        }
        if (dataNasc != null && !Objects.equals(admin.getDataNasc(), dataNasc)) {
            if (!dataNasc.isBefore(LocalDate.now().minusYears(18))) {
                throw new IllegalStateException(
                        "Data de nascimento inválida. Um administrador precisa ser maior de idade.");
            }
            admin.setDataNasc(dataNasc);
        }
        if (cpf != null && cpf.length() > 0 && !Objects.equals(admin.getCpf(), cpf)) {
            Optional<Administrador> adminOptional = adminRepository.findAdminByCpf(cpf);
            if (adminOptional.isPresent()) {
                throw new IllegalStateException("Já existe um administrador com esse CPF cadastrado.");
            }
            admin.setCpf(cpf);
        }
        if (email != null && email.length() > 0 && !Objects.equals(admin.getEmail(), email)) {
            Optional<Administrador> adminOptional = adminRepository.findAdminByEmail(email);
            if (adminOptional.isPresent()) {
                throw new IllegalStateException("Já existe um administrador com esse Email cadastrado");
            }
            admin.setEmail(email);
        }
    }

}
