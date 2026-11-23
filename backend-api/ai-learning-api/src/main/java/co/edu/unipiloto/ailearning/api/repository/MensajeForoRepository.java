package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.MensajeForo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeForoRepository
        extends JpaRepository<MensajeForo, Long> {

    List<MensajeForo>
    findByCursoIdOrderByFechaCreacionAsc(Long cursoId);
}