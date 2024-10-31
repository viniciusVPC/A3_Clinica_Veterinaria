package petmania.petmania.administrador;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    @Query("SELECT s FROM Administrador s WHERE s.cpf = ?1")
    Optional<Administrador> findAdminByCpf(String cpf);

    @Query("SELECT s FROM Administrador s WHERE s.email = ?1")
    Optional<Administrador> findAdminByEmail(String email);
}
