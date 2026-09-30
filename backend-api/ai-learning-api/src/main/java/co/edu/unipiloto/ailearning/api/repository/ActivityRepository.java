package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.Actividad;
import  org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByCursoId(Long cursoId);

}
