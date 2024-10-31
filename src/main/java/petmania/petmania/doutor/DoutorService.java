package petmania.petmania.doutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class DoutorService {
    private DoutorRepository doutorRepository;

    public DoutorService(DoutorRepository doutorRepository) {
        this.doutorRepository = doutorRepository;
    }

    public List<Doutor> getDoutores() {
        return doutorRepository.findAll();
    }

    // Regra de negócio relacionada ao cadastro de um novo doutor.
    // Verifica se já existe doutor com esse cpf ou email
    // TODO fazer o erro não pausar o programa e mostrar só uma janelinha avisando
    public void addNewDoutor(Doutor doutor) {
        Optional<Doutor> doutorOptional = doutorRepository.findDoutorByCpf(doutor.getCpf());
        if (doutorOptional.isPresent()) {
            throw new IllegalStateException("Já existe um doutor cadastrado com esse CPF");
        }
        doutorOptional = doutorRepository.findDoutorByEmail(doutor.getEmail());
        if (doutorOptional.isPresent()) {
            throw new IllegalStateException("Já existe um doutor cadastrado com esse email");
        }
        if (!doutor.getDataNasc().isBefore(LocalDate.now().minusYears(18))) {
            throw new IllegalStateException(
                    "Data de nascimento inválida. Um administrador precisa ser maior de idade.");
        }
        doutorRepository.save(doutor);
    }

    // regra de negócio relacionada à remoção de um doutor do BD.
    // verifica se existe doutor com esse id. Caso não, joga um erro.
    // se existe, deleta o doutor da TABLE
    public void deleteDoutor(Long idDoutor) {
        boolean existe = doutorRepository.existsById(idDoutor);
        if (!existe) {
            throw new IllegalStateException("Doutor com o id " + idDoutor + " não existe");
        }
        doutorRepository.deleteById(idDoutor);
    }

    // regra de negócio relacionada à edição de um doutor já existente.
    // verifica se existe doutor com esse id
    // Verifica se já não há um doutor com o mesmo cpf ou email
    // o @Transactional torna desnecessário o uso de Querys
    @Transactional
    public void updateDoutor(Long idDoutor, String nome, LocalDate dataNasc, String cpf, String email,
            String especialidade) {
        Doutor doutor = doutorRepository.findById(idDoutor)
                .orElseThrow(() -> new IllegalStateException("Doutor com o id " + idDoutor + " não existe."));
        if (nome != null && nome.length() > 0 && !Objects.equals(doutor.getNome(), nome)) {
            doutor.setNome(nome);
        }
        if (dataNasc != null && !Objects.equals(doutor.getDataNasc(), dataNasc)) {
            if (!dataNasc.isBefore(LocalDate.now().minusYears(18))) {
                throw new IllegalStateException(
                        "Data de nascimento inválida. Um doutor precisa ser maior de idade.");
            }
            doutor.setDataNasc(dataNasc);
        }
        if (cpf != null && cpf.length() > 0 && !Objects.equals(doutor.getCpf(), cpf)) {
            Optional<Doutor> doutorOptional = doutorRepository.findDoutorByCpf(cpf);
            if (doutorOptional.isPresent()) {
                throw new IllegalStateException("Já existe um doutor com esse cpf");
            }
            doutor.setCpf(cpf);
        }
        if (email != null && email.length() > 0 && !Objects.equals(doutor.getEmail(), email)) {
            Optional<Doutor> doutorOptional = doutorRepository.findDoutorByEmail(email);
            if (doutorOptional.isPresent()) {
                throw new IllegalStateException("Já existe um doutor com esse email");
            }
            doutor.setEmail(email);
        }
    }

}
