package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.Certificado;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    Optional<Certificado> findByUsuarioIdAndCursoId(
            Long usuarioId,
            Long cursoId
    );

    Optional<Certificado> findByCodigoVerificacion(
            String codigoVerificacion
    );

    List<Certificado> findByUsuarioIdOrderByFechaEmisionDesc(
            Long usuarioId
    );
}