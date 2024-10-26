//Cuida da camada de regras de negócio
package petmania.petmania.animal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<Animal> getAnimais() {
        return animalRepository.findAll();
    }

    // Regra de negócio relacionada ao cadastro de um novo animal.
    // Se já existe um animal com o mesmo nome e data de nascimento ele joga um erro
    // de "animal repetido"
    // TODO fazer o erro não pausar o programa e mostrar só uma janelinha avisando
    public void addNewAnimal(Animal animal) {
        Optional<Animal> animalDataOptional = animalRepository.findAnimalByDataNasc(animal.getDataNasc());
        Optional<Animal> animalNomeOptional = animalRepository.findAnimalByNome(animal.getNome());
        if (animalDataOptional.isPresent() && animalNomeOptional.isPresent()) {
            throw new IllegalStateException("já existe um animal com esse nome com essa data de nascimento");
        }
        animalRepository.save(animal);
    }

    // regra de negócio relacionada à remoção de um animal do BD.
    // verifica se existe animal com esse id. Caso não, joga um erro
    // se existe, deleta o animal da TABLE
    public void deleteAnimal(Long idAnimal) {
        boolean existe = animalRepository.existsById(idAnimal);
        if (!existe) {
            throw new IllegalStateException("Animal com id " + idAnimal + " não existe");
        }
        animalRepository.deleteById(idAnimal);
    }

    // regra de negócio relacionada à edição de um animal já existente.
    // verifica se existe animal com esse id. Caso não, joga um erro
    // se existe, atualiza as informações do animal na TABLE
    // o @Transactional torna desnecessário o uso de Querys
    @Transactional
    public void updateAnimal(Long idAnimal, String nome, String raca) {
        Animal animal = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new IllegalStateException("Animal com id " + idAnimal + " não existe"));
        if (nome != null && nome.length() > 0 && !Objects.equals(animal.getNome(), nome)) {
            animal.setNome(nome);
        }
        if (raca != null && raca.length() > 0 && !Objects.equals(animal.getRaca(), raca)) {
            animal.setRaca(raca);
        }
    }
}
