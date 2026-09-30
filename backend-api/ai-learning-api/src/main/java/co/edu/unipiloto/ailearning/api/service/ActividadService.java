package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.ActividadResponse;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.Actividad;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.model.Permiso;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.ActivityRepository;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    private final ActivityRepository activityRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorizacionService autorizacionService;

    public ActividadService(ActivityRepository activityRepository, CursoRepository cursoRepository,
            UsuarioRepository usuarioRepository, AutorizacionService autorizacionService) {

        this.activityRepository = activityRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorizacionService = autorizacionService;

    }

    public ActividadResponse crearActividad(Long cursoId, Actividad actividad, Long actorId) {

        Usuario actor = usuarioRepository.findById(actorId).orElseThrow(() ->
                new RuntimeException("El usuario no existe"));

        if (!autorizacionService.tienePermiso(actor, Permiso.CREAR_ACTIVIDAD)) {
            throw new AccesoDenegadoException("El usuario no tiene permiso para crear actividades");
        }

        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() ->
                new RuntimeException("El curso no existe"));

        actividad.setCurso(curso);
        Actividad guardada = activityRepository.save(actividad);

        return convertirAResponse(guardada);
    }

    public List<ActividadResponse> obtenerActividades(Long cursoId) {

        if (!cursoRepository.existsById(cursoId)) {
            throw new RuntimeException("El curso no existe");
        }

        return activityRepository.findByCursoId(cursoId).stream()
                .map(this::convertirAResponse).toList();
    }

    private ActividadResponse convertirAResponse(Actividad actividad) {
        return new ActividadResponse(actividad.getId(),
                actividad.getCurso().getId(), actividad.getDescripcion(),
                actividad.getFecha(), actividad.getPonderacion());
    }
}