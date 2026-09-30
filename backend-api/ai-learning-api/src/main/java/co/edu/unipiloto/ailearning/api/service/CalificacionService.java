package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.CalificacionResponse;
import co.edu.unipiloto.ailearning.api.dto.EstudianteCursoResponse;
import co.edu.unipiloto.ailearning.api.dto.RegistrarCalificacionRequest;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final ActivityRepository activityRepository;
    private final UsuarioRepository usuarioRepository;
    private final InscripcionRepository inscripcionRepository;
    private final AutorizacionService autorizacionService;

    public CalificacionService(
            CalificacionRepository calificacionRepository,
            ActivityRepository activityRepository,
            UsuarioRepository usuarioRepository,
            InscripcionRepository inscripcionRepository,
            AutorizacionService autorizacionService) {

        this.calificacionRepository = calificacionRepository;
        this.activityRepository = activityRepository;
        this.usuarioRepository = usuarioRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.autorizacionService = autorizacionService;
    }

    public CalificacionResponse registrar(
            Long actividadId,
            Long actorId,
            RegistrarCalificacionRequest request) {

        Usuario actor = usuarioRepository.findById(actorId)
                .orElseThrow(() ->
                        new RuntimeException("El usuario que califica no existe"));

        if (!autorizacionService.tienePermiso(
                actor,
                Permiso.REGISTRAR_CALIFICACION)) {

            throw new AccesoDenegadoException(
                    "El usuario no tiene permiso para registrar calificaciones"
            );
        }

        Actividad actividad = activityRepository
                .findById(actividadId)
                .orElseThrow(() ->
                        new RuntimeException("La actividad no existe"));

        Usuario estudiante = usuarioRepository
                .findById(request.getEstudianteId())
                .orElseThrow(() ->
                        new RuntimeException("El estudiante no existe"));

        if (estudiante.getRol() != Rol.ESTUDIANTE) {
            throw new RuntimeException(
                    "El usuario seleccionado no tiene rol ESTUDIANTE"
            );
        }

        Long cursoId = actividad.getCurso().getId();

        if (!inscripcionRepository
                .existsByUsuarioIdAndCursoId(
                        estudiante.getId(),
                        cursoId)) {

            throw new RuntimeException(
                    "El estudiante no está inscrito en el curso"
            );
        }

        if (request.getNota() == null) {
            throw new RuntimeException(
                    "La calificación es obligatoria"
            );
        }

        Calificacion calificacion =
                calificacionRepository
                        .findByActividadIdAndEstudianteId(
                                actividadId,
                                estudiante.getId()
                        )
                        .orElseGet(Calificacion::new);

        calificacion.setActividad(actividad);
        calificacion.setEstudiante(estudiante);
        calificacion.setNota(request.getNota());
        calificacion.setRetroalimentacion(
                request.getRetroalimentacion()
        );

        Calificacion guardada =
                calificacionRepository.save(calificacion);

        return convertirAResponse(guardada);
    }

    public List<CalificacionResponse> obtenerPorActividad(
            Long actividadId) {

        if (!activityRepository.existsById(actividadId)) {
            throw new RuntimeException(
                    "La actividad no existe"
            );
        }

        return calificacionRepository
                .findByActividadId(actividadId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<EstudianteCursoResponse> obtenerEstudiantesCurso(
            Long cursoId,
            Long actorId) {

        Usuario actor = usuarioRepository.findById(actorId)
                .orElseThrow(() ->
                        new RuntimeException("El usuario no existe"));

        if (!autorizacionService.tienePermiso(
                actor,
                Permiso.REGISTRAR_CALIFICACION)) {

            throw new AccesoDenegadoException(
                    "El usuario no tiene permiso para consultar estudiantes del curso"
            );
        }

        return inscripcionRepository
                .findByCursoId(cursoId)
                .stream()
                .map(inscripcion -> inscripcion.getUsuario())
                .filter(usuario ->
                        usuario.getRol() == Rol.ESTUDIANTE)
                .map(usuario ->
                        new EstudianteCursoResponse(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getCorreo()
                        ))
                .toList();
    }

    private CalificacionResponse convertirAResponse(
            Calificacion calificacion) {

        return new CalificacionResponse(
                calificacion.getId(),
                calificacion.getActividad().getId(),
                calificacion.getEstudiante().getId(),
                calificacion.getEstudiante().getNombre(),
                calificacion.getNota(),
                calificacion.getRetroalimentacion(),
                calificacion.getFechaCalificacion()
        );
    }
}