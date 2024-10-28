//Cuida das regras de negócio
package petmania.petmania.consulta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import petmania.petmania.animal.Animal;
import petmania.petmania.animal.AnimalRepository;
import petmania.petmania.cliente.Cliente;
import petmania.petmania.cliente.ClienteRepository;
import petmania.petmania.doutor.Doutor;
import petmania.petmania.doutor.DoutorRepository;

@Service
public class ConsultaService {
    private ConsultaRepository consultaRepository;
    private ClienteRepository clienteRepository;
    private AnimalRepository animalRepository;
    private DoutorRepository doutorRepository;

    public ConsultaService(ConsultaRepository consultaRepository, ClienteRepository clienteRepository,
            AnimalRepository animalRepository, DoutorRepository doutorRepository) {
        this.consultaRepository = consultaRepository;
        this.clienteRepository = clienteRepository;
        this.animalRepository = animalRepository;
        this.doutorRepository = doutorRepository;
    }

    public List<Consulta> getConsultas() {
        return consultaRepository.findAll();
    }

    // Regra de negócio que verifica se há consulta marcada no mesmo horário.
    // TODO criar uma distância mínima entre horários de consultas de 30 minutos
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

    // Regra de negócio que edita os valores da consulta
    // Verifica se existe consulta
    // Verifica se já tem consulta marcada pra esse horário
    // TODO criar uma distância mínima entre horários de consultas de 30 minutos
    @Transactional
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

    // Conecta cliente, animal e doutor à consulta
    // Verifica se consulta, cliente, animal e doutor existem
    // Verifica se animal pertence a cliente
    // TODO verificar se doutor tem horário disponível
    @Transactional
    public void conectaClienteAnimalEDoutorAConsulta(Long idConsulta, Long idCliente, Long idAnimal, Long idDoutor) {
        Set<Animal> pets = null;
        Set<Consulta> consultasCliente = null;
        Set<Consulta> consultasAnimal = null;
        Set<Consulta> consultasDoutor = null;
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new IllegalStateException("Consulta com id " + idConsulta + " não existe."));
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + idCliente + " não existe"));
        Animal animal = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe"));
        Doutor doutor = doutorRepository.findById(idDoutor)
                .orElseThrow(() -> new IllegalStateException("Doutor com id " + idDoutor + " não existe"));
        pets = cliente.getPets();
        consultasCliente = cliente.getConsultas();
        consultasAnimal = animal.getConsultas();
        consultasDoutor = doutor.getConsultas();
        if (!pets.contains(animal)) {
            throw new IllegalStateException("Esse animal não pertence a esse cliente.");
        }
        consultasCliente.add(consulta);
        consultasAnimal.add(consulta);
        consultasDoutor.add(consulta);
        cliente.setConsultas(consultasCliente);
        animal.setConsultas(consultasAnimal);
        doutor.setConsultas(consultasDoutor);
    }

}
