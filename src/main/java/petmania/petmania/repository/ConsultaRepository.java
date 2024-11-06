package petmania.petmania.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import petmania.petmania.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT s FROM Consulta s WHERE s.horario = ?1")
    Optional<Consulta> findConsultaByHorario(LocalDateTime horario);
}
