package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
            String nombre,
            String descripcion,
            String categoria
    );
}
