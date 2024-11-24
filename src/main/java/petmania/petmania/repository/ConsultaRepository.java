package petmania.petmania.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import petmania.petmania.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // @Query("SELECT s FROM Consulta s WHERE s.horario = ?1")
    Optional<Consulta> findConsultaByHorario(LocalDateTime horario);

    @Query(value = "SELECT c.* FROM consulta c WHERE c.id_cliente = ?1", nativeQuery = true)
    List<Consulta> getConsultasByCliente(Long id_cliente);

    @Query(value = "SELECT c.* FROM consulta c WHERE c.id_doutor = ?1", nativeQuery = true)
    List<Consulta> getConsultasByDoutor(Long id_Doutor);
}
