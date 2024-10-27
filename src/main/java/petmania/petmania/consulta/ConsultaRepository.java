package petmania.petmania.consulta;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT s FROM Consulta s WHERE s.horario = ?1")
    Optional<Consulta> findConsultaByHorario(LocalDateTime horario);
}
