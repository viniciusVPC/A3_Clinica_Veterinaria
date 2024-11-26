//Cuida da camada API
package petmania.petmania.controller;

import java.util.Optional;
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
    public String mostraFormularioConsulta(Model model, @RequestParam String cpfCliente,
            @RequestParam String cpfDoutor) {
        ConsultaDTO consultaDTO = new ConsultaDTO(cpfCliente, cpfDoutor);
        model.addAttribute("consultaDto", consultaDTO);
        return "/consultas/add-consulta";
    }

    @PostMapping("/addconsulta")
    public String addConsulta(@Valid @ModelAttribute("consultaDto") ConsultaDTO consultaDto, BindingResult result,
            Model model) {
        boolean error = false;

        // Verifica se o objeto consulta foi criado sem erros
        if (result.hasErrors()) {
            return "/consultas/add-consulta";
        }

        // Procura no BD o cliente pelo CPF
        Optional<Cliente> cliente = clienteRepo.findClienteByCpf(consultaDto.getCpfCliente());
        if (!cliente.isPresent()) {
            model.addAttribute("errorCPFCliente", "Não existe cliente com esse CPF.");
            error = true;
        }

        // TODO SE CLIENTE FOR NULO VAI DAR ERRO NA HORA DE PROCURAR ANIMAL
        System.out.println("id cliente: " + cliente.get().getId());
        // Procura no BD o animal pelo cliente e depois por nome
        var pets = animalRepo.getAnimalByDono(cliente.get().getId());
        Animal animal = null;
        for (Animal pet : pets) {
            if (pet.getNome().equals(consultaDto.getNomeAnimal())) {
                animal = pet;
            }
        }
        if (animal == null) {
            model.addAttribute("errorNomeAnimal", "Esse animal não pertence a esse cliente.");
            error = true;
        }

        // Procura no BD o doutor pelo CPF
        Optional<Doutor> doutor = doutorRepo.findDoutorByCpf(consultaDto.getCpfDoutor());
        if (!doutor.isPresent()) {
            model.addAttribute("errorCPFDoutor", "Não existe doutor com esse CPF.");
            error = true;
        }

        // TODO verificar disponibilidade do doutor

        if (consultaDto.getDuracaoEmMinutos() <= 5) {
            model.addAttribute("errorDuracao", "Todas as consultas devem durar mais que 5 minutos.");
            error = true;
        }

        if (error)
            return "/consultas/add-consulta";

        // Cria uma consulta nova
        Consulta consulta = new Consulta(cliente.get(), animal, doutor.get(), consultaDto.getTipo(),
                consultaDto.getHorario(),
                consultaDto.getDuracaoEmMinutos());

        // Adiciona consulta no histórico de consultas de todos
        Set<Consulta> consultas = cliente.get().getConsultas();
        consultas.add(consulta);
        cliente.get().setConsultas(consultas);

        consultas = animal.getConsultas();
        consultas.add(consulta);
        animal.setConsultas(consultas);

        consultas = doutor.get().getConsultas();
        consultas.add(consulta);
        doutor.get().setConsultas(consultas);

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

    @GetMapping("/cliente")
    public String mostraListaConsultasCliente(Model model, @RequestParam Long idCliente) {
        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente com id " + idCliente + " não encontrado."));
        var consultas = repo.getConsultasByCliente(idCliente);
        model.addAttribute("consultas", consultas);
        model.addAttribute("cliente", cliente);
        return "/consultas/index";
    }

    @GetMapping("/doutor")
    public String mostraListaConsultasDoutor(Model model, @RequestParam Long idDoutor) {
        Doutor doutor = doutorRepo.findById(idDoutor)
                .orElseThrow(() -> new IllegalStateException("Doutor com id " + idDoutor + " não encontrado."));
        var consultas = repo.getConsultasByDoutor(idDoutor);
        model.addAttribute("consultas", consultas);
        model.addAttribute("doutor", doutor);
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
        boolean error = false;

        if (result.hasErrors()) {
            return "consultas/update-consulta";
        }

        Consulta consulta = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Consulta com id " + id + " não existe."));

        Optional<Cliente> cliente = clienteRepo.findClienteByCpf(consultaDto.getCpfCliente());
        if (!cliente.isPresent()) {
            model.addAttribute("errorCPFCliente", "Não existe cliente com esse CPF.");
            error = true;
        }

        // TODO SE CLIENTE FOR NULO VAI DAR ERRO NA HORA DE PROCURAR ANIMAL

        // Procura no BD o animal pelo cliente e depois por nome
        var pets = animalRepo.getAnimalByDono(cliente.get().getId());
        Animal animal = null;
        for (Animal pet : pets) {
            if (pet.getNome().equals(consultaDto.getNomeAnimal())) {
                animal = pet;
            }
        }
        if (animal == null) {
            model.addAttribute("errorNomeAnimal", "Esse animal não pertence a esse cliente.");
            error = true;
        }
        // Procura no BD o doutor pelo CPF
        Optional<Doutor> doutor = doutorRepo.findDoutorByCpf(consultaDto.getCpfDoutor());
        if (!doutor.isPresent()) {
            model.addAttribute("errorCPFDoutor", "Não existe doutor com esse CPF.");
            error = true;
        }

        // TODO verificar disponibilidade do doutor

        if (consultaDto.getDuracaoEmMinutos() <= 5) {
            model.addAttribute("errorDuracao", "Todas as consultas devem durar mais que 5 minutos.");
            error = true;
        }

        if (error)
            return "/consultas/add-consulta";

        // Cria uma consulta nova
        consulta = new Consulta(cliente.get(), animal, doutor.get(), consultaDto.getTipo(), consultaDto.getHorario(),
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
