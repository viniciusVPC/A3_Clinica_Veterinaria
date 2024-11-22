//Cuida da camada API
package petmania.petmania.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import petmania.petmania.dto.ConsultaDTO;
import petmania.petmania.model.Animal;
import petmania.petmania.model.Cliente;
import petmania.petmania.model.Consulta;
import petmania.petmania.model.Doutor;
import petmania.petmania.repository.AnimalRepository;
import petmania.petmania.repository.ClienteRepository;
import petmania.petmania.repository.ConsultaRepository;
import petmania.petmania.repository.DoutorRepository;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository repo;
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private DoutorRepository doutorRepo;
    @Autowired
    private AnimalRepository animalRepo;

    @GetMapping("/create")
    public String mostraFormularioConsulta(Model model) {
        ConsultaDTO consultaDTO = new ConsultaDTO();
        model.addAttribute("consultaDto", consultaDTO);
        return "/consultas/add-consulta";
    }

    @PostMapping("/addconsulta")
    public String addConsulta(@Valid @ModelAttribute("consultaDto") ConsultaDTO consultaDto, BindingResult result,
            Model model) {
        // Verifica se o objeto consulta foi criado sem erros
        if (result.hasErrors()) {
            return "/consultas/add-consulta";
        }

        // Procura no BD o cliente pelo CPF
        Cliente cliente = clienteRepo.findClienteByCpf(consultaDto.getCpfCliente())
                .orElseThrow(() -> new IllegalStateException(
                        "Cliente com o CPF " + consultaDto.getCpfCliente() + " não existe."));

        System.out.println("id cliente: " + cliente.getId());
        // Procura no BD o animal pelo cliente e depois por nome
        var pets = animalRepo.getAnimalByDono(cliente.getId());
        Animal animal = null;
        for (Animal pet : pets) {
            System.out.println("nome pet cliente: " + pet.getNome());
            System.out.println("nome pet desejado: " + consultaDto.getNomeAnimal());
            if (pet.getNome().equals(consultaDto.getNomeAnimal())) {
                animal = pet;
            }
        }
        if (animal == null)
            throw new IllegalStateException("Esse animal não pertence a esse cliente.");

        // Procura no BD o doutor pelo CPF
        Doutor doutor = doutorRepo.findDoutorByCpf(consultaDto.getCpfDoutor())
                .orElseThrow(() -> new IllegalStateException(
                        "Doutor com o CPF " + consultaDto.getCpfDoutor() + " não existe."));

        // Cria uma consulta nova
        Consulta consulta = new Consulta(cliente, animal, doutor, consultaDto.getTipo(), consultaDto.getHorario(),
                consultaDto.getDuracaoEmMinutos());

        // Adiciona consulta no histórico de consultas de todos
        Set<Consulta> consultas = cliente.getConsultas();
        consultas.add(consulta);
        cliente.setConsultas(consultas);

        consultas = animal.getConsultas();
        consultas.add(consulta);
        animal.setConsultas(consultas);

        consultas = doutor.getConsultas();
        consultas.add(consulta);
        doutor.setConsultas(consultas);

        model.addAttribute("consulta", consultas);
        repo.save(consulta);
        return "redirect:/consultas";
    }

    @GetMapping({ "", "/" })
    public String mostraListaConsultas(Model model) {
        var consultas = repo.findAll(Sort.by(Sort.Direction.ASC, "idConsulta"));
        model.addAttribute("consultas", consultas);
        return "/consultas/index";
    }

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long id, Model model) {
        Consulta consulta = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Consulta com id " + id + " não existe."));
        ConsultaDTO consultaDto = new ConsultaDTO(consulta.getCliente().getCpf(), consulta.getAnimal().getNome(),
                consulta.getDoutor().getCpf(), consulta.getTipo(), consulta.getHorario(),
                consulta.getDuracaoEmMinutos());
        model.addAttribute("consulta", consulta);
        model.addAttribute("consultaDto", consultaDto);
        return "/consultas/update-consulta";
    }

    @PostMapping("edit")
    public String updateConsulta(@RequestParam Long id, @Valid @ModelAttribute("consultaDto") ConsultaDTO consultaDto,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "consultas/update-consulta";
        }
        Consulta consulta = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Consulta com id " + id + " não existe."));

        // Procura no BD o cliente pelo CPF
        Cliente cliente = clienteRepo.findClienteByCpf(consultaDto.getCpfCliente())
                .orElseThrow(() -> new IllegalStateException(
                        "Cliente com o CPF " + consultaDto.getCpfCliente() + " não existe."));

        // Procura no BD o animal pelo cliente e depois por nome
        var pets = animalRepo.getAnimalByDono(cliente.getId());
        Animal animal = null;
        for (Animal pet : pets) {
            if (pet.getNome() == consultaDto.getNomeAnimal())
                animal = pet;
        }
        if (animal == null)
            throw new IllegalStateException("Esse animal não pertence a esse cliente.");

        // Procura no BD o doutor pelo CPF
        Doutor doutor = doutorRepo.findDoutorByCpf(consultaDto.getCpfDoutor())
                .orElseThrow(() -> new IllegalStateException(
                        "Doutor com o CPF " + consultaDto.getCpfDoutor() + " não existe."));

        // Cria uma consulta nova
        consulta = new Consulta(cliente, animal, doutor, consultaDto.getTipo(), consultaDto.getHorario(),
                consultaDto.getDuracaoEmMinutos());
        consulta.setIdConsulta(id);
        repo.save(consulta);
        return "redirect:/consultas";
    }

    @GetMapping("/delete/{id}")
    public String deleteConsulta(@PathVariable("id") Long id, Model model) {
        Consulta consulta = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Consulta com id " + id + " não existe."));
        Set<Consulta> consultas = consulta.getCliente().getConsultas();
        consultas.remove(consulta);
        consulta.getCliente().setConsultas(consultas);

        consultas = consulta.getAnimal().getConsultas();
        consultas.remove(consulta);
        consulta.getAnimal().setConsultas(consultas);

        consultas = consulta.getDoutor().getConsultas();
        consultas.remove(consulta);
        consulta.getDoutor().setConsultas(consultas);

        repo.delete(consulta);
        return "redirect:/consultas";
    }

}
