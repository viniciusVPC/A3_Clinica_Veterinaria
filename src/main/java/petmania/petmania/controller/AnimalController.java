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
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalRepository repo;
    @Autowired
    private ClienteRepository donoRepo;

    @GetMapping("/signup")
    public String mostraFormularioSignUp(Model model, @RequestParam Long id) {
        Cliente dono = donoRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + id + " não existe."));

        AnimalDTO animalDto = new AnimalDTO(id);
        model.addAttribute("animalDto", animalDto);
        model.addAttribute("idDono", dono.getId());
        return "/animais/add-animal";
    }

    
    // @RequestParam Long id,
    @PostMapping("/addanimal")
    public String addAnimal(@Valid @ModelAttribute("animalDto") AnimalDTO animalDto,
            BindingResult result, Model model) {
        boolean error = false;
        if (result.hasErrors()) {

            //model.addAttribute("idDono", id);
            model.addAttribute("animalDto", animalDto);
            return "/animais/add-animal";

        }
        /* Cliente dono = donoRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + id +
                        " não existe.")); */

        Long id = animalDto.getIdDono();

        /* for (Animal pet : dono.getPets()) {
            if (pet.getNome().equals(animalDto.getNome()) && pet.getIdade() == animalDto.getIdade()) {
                model.addAttribute("errorAnimalRepetido",
                        "Esse cliente parece já ter esse animal.");
                error = true;
            }
        } */

        if (error) {
            //model.addAttribute("idDono", id);
            model.addAttribute("animalDto", animalDto);
            return String.format("animais/add-animal?id=%d", 1);
        }

        Animal animal = new Animal(animalDto.getNome(), animalDto.getDataNasc(),
                animalDto.getEspecie(), animalDto.getRaca());

        /* Set<Animal> pets = dono.getPets();
        pets.add(animal);
        dono.setPets(pets); */

        //model.addAttribute("cliente", dono);
        repo.save(animal);
        return String.format("redirect:/animais?id=%d", id);
    }

    @GetMapping({ "", "/" })
    public String mostraListaAnimais(Model model, @RequestParam Long id) {

        Cliente dono = donoRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + id + " não existe."));

        var animais = repo.getAnimalByDono(id);
        model.addAttribute("animais", animais);
        model.addAttribute("cliente", dono);
        return "/animais/index";
    }

    @GetMapping("/edit")
    public String mostraFormularioUpdate(@RequestParam Long idAnimal, @RequestParam Long idDono, Model model) {
        Animal animal = repo.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe."));
        AnimalDTO animalDto = new AnimalDTO(animal.getNome(), animal.getDataNasc(), animal.getEspecie(),
                animal.getRaca(), idDono);
        model.addAttribute("animal", animal);
        model.addAttribute("animalDto", animalDto);
        return "/animais/update-animal";
    }

    @PostMapping("/edit")
    public String updateAnimal(@RequestParam Long idAnimal, @RequestParam Long idDono,
            @Valid @ModelAttribute("animalDto") AnimalDTO animalDto,
            BindingResult result, Model model) {
        boolean error = false;

        Animal animal = repo.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe."));

        if (result.hasErrors()) {
            return "/animais/update-animal";
        }

        Cliente dono = donoRepo.findById(idDono)
                .orElseThrow(() -> new IllegalStateException("Dono com id " + idDono + " não existe."));

        for (Animal pet : dono.getPets()) {
            if (pet.getNome().equals(animalDto.getNome()) && pet.getIdade() == animalDto.getIdade()) {
                if (!pet.equals(animal)) {
                    model.addAttribute("errorAnimalRepetido", "Esse cliente parece já ter esse animal.");
                    error = true;
                }
            }
        }

        if (error)
            return "/animais/update-animal";

        animal = new Animal(animalDto.getNome(), animalDto.getDataNasc(), animalDto.getEspecie(), animalDto.getRaca());
        animal.setId(idAnimal);
        repo.save(animal);
        return String.format("redirect:/animais?id=%d", idDono);
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
        return String.format("redirect:/animais?id=%d", idDono);
    }

}
