package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.ReporteResumenResponse;
import co.edu.unipiloto.ailearning.api.repository.ActivityRepository;
import co.edu.unipiloto.ailearning.api.repository.CertificadoRepository;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.InscripcionRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ActivityRepository activityRepository;
    private final CertificadoRepository certificadoRepository;

    public ReporteService(
            UsuarioRepository usuarioRepository,
            CursoRepository cursoRepository,
            InscripcionRepository inscripcionRepository,
            ActivityRepository activityRepository,
            CertificadoRepository certificadoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.activityRepository = activityRepository;
        this.certificadoRepository = certificadoRepository;
    }

    public ReporteResumenResponse obtenerResumen() {

        return new ReporteResumenResponse(
                usuarioRepository.count(),
                cursoRepository.count(),
                inscripcionRepository.count(),
                activityRepository.count(),
                certificadoRepository.count()
        );
    }
}