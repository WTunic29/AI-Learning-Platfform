package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.Permiso;
import co.edu.unipiloto.ailearning.api.model.UsuarioPermiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioPermisoRepository extends JpaRepository<UsuarioPermiso, Long> {

    List<UsuarioPermiso> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndPermiso(Long usuarioId, Permiso permiso);
    void deleteByUsuarioIdAndPermiso(Long usuarioId, Permiso permiso);

}
