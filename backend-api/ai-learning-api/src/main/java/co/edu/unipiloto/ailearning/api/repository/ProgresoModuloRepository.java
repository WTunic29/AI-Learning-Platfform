package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.ProgresoModulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
*
* findByUsuarioId: obtiene los módulos completados por un estudiante.
* findByUsuarioIdAndModuloCursoId: consulta su progreso dentro de un curso concreto.
* findByUsuarioIdAndModuloId: busca el registro de un módulo específico.
* existsByUsuarioIdAndModuloId: permite comprobar si ya está completado.
* countByUsuarioIdAndModuloCursoId: cuenta los módulos completados de un curso.
*
* */

public interface ProgresoModuloRepository extends JpaRepository<ProgresoModulo, Long> {

    List<ProgresoModulo> findByUsuarioId(Long usuarioId);
    List<ProgresoModulo> findByUsuarioIdAndModuloCursoId(Long usuarioId, Long cursoId);
    Optional<ProgresoModulo> findByUsuarioIdAndModuloId(Long usuarioId, Long moduloId);

    boolean existsByUsuarioIdAndModuloId(Long usuarioId, Long moduloId);
    long countByUsuarioIdAndModuloCursoId(Long usuarioId, Long cursoId);

}
