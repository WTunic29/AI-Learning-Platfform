package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.EntregaActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaActividadRepository
        extends JpaRepository<EntregaActividad, Long> {

    List<EntregaActividad>
    findByActividadIdOrderByFechaEntregaDesc(Long actividadId);
}