package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalificacionRepository
        extends JpaRepository<Calificacion, Long> {

    Optional<Calificacion>
    findByActividadIdAndEstudianteId(
            Long actividadId,
            Long estudianteId
    );

    List<Calificacion> findByActividadId(Long actividadId);
}