package petmania.petmania.cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT s FROM Cliente s WHERE s.cpf = ?1")
    Optional<Cliente> findClienteByCpf(String cpf);

    @Query("SELECT s FROM Cliente s WHERE s.email = ?1")
    Optional<Cliente> findClienteByEmail(String email);
}