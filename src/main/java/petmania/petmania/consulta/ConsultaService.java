//Cuida das regras de negócio
package petmania.petmania.consulta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ConsultaService {
    private ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> getConsultas() {
        return consultaRepository.findAll();
    }

    // Regra de negócio que verifica se há consulta marcada no mesmo horário.
    // Caso sim, lança um erro. Caso não, salva a consulta
    // TODO Fazer o erro não parar o programa, só mostrar uma janela
    public void addNewConsulta(Consulta consulta) {
        Optional<Consulta> consultaOptional = consultaRepository.findConsultaByHorario(consulta.getHorario());
        if (consultaOptional.isPresent()) {
            throw new IllegalStateException("Já há uma consulta marcada pra esse horário.");
        }
        consultaRepository.save(consulta);
    }

    // Regra de negócio que verifica se existe consulta com esse id.
    // Se sim, lança um erro. Se não, deleta a consulta da TABLE
    public void deleteConsulta(Long idConsulta) {
        boolean existe = consultaRepository.existsById(idConsulta);
        if (!existe) {
            throw new IllegalStateException("Consulta com id " + idConsulta + " não existe.");
        }
        consultaRepository.deleteById(idConsulta);
    }

    public void updateConsulta(Long idConsulta, String tipo, LocalDateTime horario) {
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new IllegalStateException("Consulta com id " + idConsulta + " não existe."));
        if (tipo != null && tipo.length() > 0 && !Objects.equals(consulta.getTipo(), tipo)) {
            consulta.setTipo(tipo);
        }
        if (horario != null && !Objects.equals(consulta.getHorario(), horario)) {
            Optional<Consulta> consultaOptional = consultaRepository.findConsultaByHorario(consulta.getHorario());
            if (consultaOptional.isPresent()) {
                throw new IllegalStateException("Já há uma consulta marcada pra esse horário.");
            }
            consulta.setHorario(horario);
        }
    }

}
