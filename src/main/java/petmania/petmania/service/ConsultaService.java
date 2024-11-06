//Cuida das regras de negócio
package petmania.petmania.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import petmania.petmania.model.Animal;
import petmania.petmania.model.Cliente;
import petmania.petmania.model.Consulta;
import petmania.petmania.model.Doutor;
import petmania.petmania.repository.AnimalRepository;
import petmania.petmania.repository.ClienteRepository;
import petmania.petmania.repository.ConsultaRepository;
import petmania.petmania.repository.DoutorRepository;

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

    // TODO botão que verifica se a data de todas as consultas já passaram da data
    // atual do dia, colocando elas como "vencidas" ou "passadas.

    // Toda consulta deve ser criada conectando um cliente, animal e doutor à mesma
    // verifica se cliente, animal e doutor existem
    // Verifica se animal pertence a cliente
    // Roda um for durante todas as consultas do doutor e verifica se a nova
    // consulta está marcada para um horário com diferença maior que
    // 30 minutos entre consultas do doutor
    // se tudo está correto, cria consulta e conecta ela à todos
    public void addNewConsulta(Consulta consulta, Long idCliente, Long idAnimal, Long idDoutor) {
        Set<Animal> pets = null;
        Set<Consulta> consultasCliente = null;
        Set<Consulta> consultasAnimal = null;
        Set<Consulta> consultasDoutor = null;
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalStateException(
                        "Cliente com id " + idCliente + " não existe"));
        Animal animal = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException(
                        "Animal com id " + idAnimal + " não existe"));
        Doutor doutor = doutorRepository.findById(idDoutor)
                .orElseThrow(() -> new IllegalStateException(
                        "Doutor com id " + idDoutor + " não existe"));
        pets = cliente.getPets();

        if (!pets.contains(animal)) {
            throw new IllegalStateException("Esse animal não pertence a esse cliente.");
        }
        consultasDoutor = doutor.getConsultas();
        for (Consulta consultaDoutor : consultasDoutor) {
            if (consulta.getHorario().plusMinutes(consulta.getDuracaoEmMinutos() + 15)
                    .compareTo(consultaDoutor.getHorario()) >= 0
                    && consultaDoutor.getHorario().plusMinutes(consultaDoutor.getDuracaoEmMinutos() + 15)
                            .compareTo(consulta.getHorario()) >= 0)
                throw new IllegalStateException(
                        "Doutor indisponível. Consultas devem ser marcadas com um intervalo de 15 minutos entre si");
        }
        consultasCliente = cliente.getConsultas();
        consultasAnimal = animal.getConsultas();

        consultasCliente.add(consulta);
        consultasAnimal.add(consulta);
        consultasDoutor.add(consulta);
        consultaRepository.save(consulta);
        cliente.setConsultas(consultasCliente);
        animal.setConsultas(consultasAnimal);
        doutor.setConsultas(consultasDoutor);
    }

    // Regra de negócio que verifica se existe consulta com esse id.
    // Verifica se existem os cliente, animal e doutor dessa consulta
    // Se sim, lança um erro. Se não, deleta a consulta da TABLE
    // Deleta também a consulta dos históricos de todos
    public void deleteConsulta(Long idConsulta) {
        Set<Consulta> consultasCliente = null;
        Set<Consulta> consultasAnimal = null;
        Set<Consulta> consultasDoutor = null;
        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new IllegalStateException(
                        "Consulta com id " + idConsulta + " não existe."));

        Cliente cliente = clienteRepository.findById(consulta.getCliente().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Cliente com id " + consulta.getCliente().getId() + " não existe"));
        Animal animal = animalRepository.findById(consulta.getAnimal().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Animal com id " + consulta.getAnimal().getId() + " não existe"));
        Doutor doutor = doutorRepository.findById(consulta.getDoutor().getIdDoutor())
                .orElseThrow(() -> new IllegalStateException(
                        "Doutor com id " + consulta.getDoutor().getIdDoutor() + " não existe"));

        consultasCliente = cliente.getConsultas();
        consultasAnimal = animal.getConsultas();
        consultasDoutor = doutor.getConsultas();
        consultasCliente.remove(consulta);
        consultasAnimal.remove(consulta);
        consultasDoutor.remove(consulta);
        cliente.setConsultas(consultasCliente);
        animal.setConsultas(consultasAnimal);
        doutor.setConsultas(consultasDoutor);

        consultaRepository.deleteById(idConsulta);

    }

    // Regra de negócio que edita os valores da consulta
    // Verifica se existe consulta
    // Roda um for durante todas as consultas do doutor e verifica se a nova
    // consulta está marcada para um horário com diferença maior que
    // 30 minutos entre consultas do doutor
    @Transactional
    public void updateConsulta(Long idConsulta, String tipo, LocalDateTime horario, Integer duracaoEmMinutos) {
        Set<Consulta> consultasDoutor = null;

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new IllegalStateException(
                        "Consulta com id " + idConsulta + " não existe."));
        int duracaoTemp;
        if (duracaoEmMinutos != null && duracaoEmMinutos > 0) {
            duracaoTemp = duracaoEmMinutos;
        } else
            duracaoTemp = consulta.getDuracaoEmMinutos();
        Doutor doutor = consulta.getDoutor();
        if (horario != null && !Objects.equals(consulta.getHorario(), horario)) {
            consultasDoutor = doutor.getConsultas();
            for (Consulta consultaDoutor : consultasDoutor) {
                if (horario.plusMinutes(duracaoTemp + 15)
                        .compareTo(consultaDoutor.getHorario()) >= 0
                        && consultaDoutor.getHorario().plusMinutes(consultaDoutor.getDuracaoEmMinutos() + 15)
                                .compareTo(horario) >= 0)
                    throw new IllegalStateException(
                            "Doutor indisponível. Consultas devem ser marcadas com um intervalo de 15 minutos entre si");
            }
            consulta.setHorario(horario);
        }

        if (tipo != null && tipo.length() > 0 && !Objects.equals(consulta.getTipo(), tipo)) {
            consulta.setTipo(tipo);
        }

    }
}