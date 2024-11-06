package petmania.petmania.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import petmania.petmania.model.Animal;

//define qual o tipo ao qual esse repositório se refere, além da variável de ID do mesmo (Long)
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // Utilizar esse modelo de repositório nos poupa trabalho na hora de criar
    // funções de get, post, etc.

    @Query("SELECT s FROM Animal s WHERE s.dataNasc = ?1")
    Optional<Animal> findAnimalByDataNasc(LocalDate dataNasc);

    @Query("SELECT s FROM Animal s WHERE s.nome = ?1")
    Optional<Animal> findAnimalByNome(String nome);
}
