package petmania.petmania.doutor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DoutorRepository extends JpaRepository<Doutor, Long> {

    @Query("SELECT s FROM Doutor s WHERE s.cpf = ?1")
    Optional<Doutor> findDoutorByCpf(String cpf);

    @Query("SELECT s FROM Doutor s WHERE s.email = ?1")
    Optional<Doutor> findDoutorByEmail(String email);
}
