//Cuida da camada de API
package petmania.petmania.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import petmania.petmania.dto.AnimalDTO;
import petmania.petmania.model.Animal;
import petmania.petmania.model.Cliente;
import petmania.petmania.repository.AnimalRepository;
import petmania.petmania.repository.ClienteRepository;

@Controller
@RequestMapping("/clientes/animais")
public class AnimalController {

    @Autowired
    private AnimalRepository repo;
    @Autowired
    private ClienteRepository donoRepo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Model model, @RequestParam Long id) {

        Cliente dono = donoRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + id + " não existe."));

        AnimalDTO animalDto = new AnimalDTO();
        model.addAttribute("animalDto", animalDto);
        model.addAttribute("cliente", dono);
        return "/clientes/animais/add-animal";
    }

    @PostMapping("/addanimal")
    public String addAnimal(@Valid @ModelAttribute("animalDto") AnimalDTO animalDto, @RequestParam Long id,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "/clientes/animais/add-animal";
        }

        Cliente dono = donoRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + id + " não existe."));

        Animal animal = new Animal(animalDto.getNome(), animalDto.getDataNasc(),
                animalDto.getEspecie(), animalDto.getRaca());

        Set<Animal> pets = dono.getPets();
        pets.add(animal);
        dono.setPets(pets);

        model.addAttribute("cliente", dono);
        repo.save(animal);
        return String.format("redirect:/clientes/animais?id=%d", id);
    }

    @GetMapping({ "", "/" })
    public String mostraListaAnimais(Model model, @RequestParam Long id) {

        Cliente dono = donoRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + id + " não existe."));

        var animais = repo.getAnimalByDono(id);
        model.addAttribute("animais", animais);
        model.addAttribute("cliente", dono);
        return "/clientes/animais/index";
    }

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long idAnimal, @RequestParam Long idDono, Model model) {
        Animal animal = repo.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe."));
        AnimalDTO animalDto = new AnimalDTO(animal.getNome(), animal.getDataNasc(), animal.getEspecie(),
                animal.getRaca());
        model.addAttribute("animal", animal);
        model.addAttribute("animalDto", animalDto);
        return "/clientes/animais/update-animal";
    }

    @PostMapping("/edit")
    public String updateAnimal(@RequestParam Long idAnimal, @RequestParam Long idDono,
            @Valid @ModelAttribute("animalDto") AnimalDTO animalDto,
            BindingResult result, Model model) {
        Animal animal = repo.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe."));
        if (result.hasErrors()) {
            return "/clientes/animais/update-animal";
        }
        animal = new Animal(animalDto.getNome(), animalDto.getDataNasc(), animalDto.getEspecie(), animalDto.getRaca());
        animal.setId(idAnimal);
        repo.save(animal);
        return String.format("redirect:/clientes/animais?id=%d", idDono);
    }

    @GetMapping("/delete")
    public String deleteAnimal(@RequestParam Long idDono, @RequestParam Long idAnimal, Model model) {
        Animal animal = repo.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe."));
        Cliente dono = donoRepo.findById(idDono)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + idDono + " não existe."));
        Set<Animal> pets = dono.getPets();
        pets.remove(animal);
        dono.setPets(pets);
        repo.delete(animal);
        return String.format("redirect:/clientes/animais?id=%d", idDono);
    }

}
