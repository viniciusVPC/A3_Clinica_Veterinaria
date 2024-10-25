//Cuida da camada de regras de negócio
package petmania.petmania.animal;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AnimalService {
    public List<Animal> getAnimais() {
        return List.of(
                new Animal(
                        1L,
                        "Baumi",
                        6,
                        "gato",
                        "vira-lata",
                        1L));
    }
}
