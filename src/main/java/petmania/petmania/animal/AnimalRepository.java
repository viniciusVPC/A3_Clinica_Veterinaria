package petmania.petmania.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//define qual o tipo ao qual esse repositório se refere, além da variável de ID do mesmo (Long)
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // Utilizar esse modelo de repositório nos poupa trabalho na hora de criar
    // funções de get, post, etc.
}
