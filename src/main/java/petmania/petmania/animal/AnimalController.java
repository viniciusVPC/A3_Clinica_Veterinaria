//Cuida da camada de API
package petmania.petmania.animal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/v1/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // função GET da api
    @GetMapping
    public List<Animal> getAnimais() {
        return animalService.getAnimais();
    }

    // função POST da api
    @PostMapping
    // RequestBody mapeia automaticamente o objeto Animal do BODY do POST da api
    public void registraNovoAnimal(@RequestBody Animal animal) {
        animalService.addNewAnimal(animal);
    }

    // função DELETE da api
    @DeleteMapping(path = "{idAnimal}")
    public void deleteAnimal(@PathVariable("idAnimal") Long idAnimal) {
        // TODO verificar id not null
        animalService.deleteAnimal(idAnimal);
    }

    // função PUT da api
    @PutMapping(path = "{idAnimal}")
    public void updateAnimal(@PathVariable("idAnimal") Long idAnimal,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String raca) {
        animalService.updateAnimal(idAnimal, nome, raca);
    }
}
