package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.ActividadResponse;
import co.edu.unipiloto.ailearning.api.dto.ActividadRequest;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ActividadService {

    private final ActivityRepository activityRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorizacionService autorizacionService;

    public ActividadService(
            ActivityRepository activityRepository,
            CursoRepository cursoRepository,
            UsuarioRepository usuarioRepository,
            AutorizacionService autorizacionService) {

        this.activityRepository = activityRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorizacionService = autorizacionService;
    }

    public ActividadResponse crearActividad(
            Long cursoId,
            ActividadRequest datos,
            Long actorId) {

        validarPermiso(actorId);

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El curso no existe"));

        validarActividad(datos);

        Actividad actividad = new Actividad();
        actividad.setDescripcion(datos.descripcion());
        actividad.setFecha(datos.fecha());
        actividad.setPonderacion(datos.ponderacion());
        actividad.setCurso(curso);

        return convertirAResponse(
                activityRepository.save(actividad));
    }

    public ActividadResponse obtenerPorId(Long id) {

        return convertirAResponse(
                buscarActividad(id));
    }

    public List<ActividadResponse> obtenerActividades(Long cursoId) {

        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El curso no existe");
        }

        return activityRepository.findByCursoId(cursoId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ActividadResponse actualizar(
            Long id,
            ActividadRequest datos,
            Long actorId) {

        validarPermiso(actorId);
        validarActividad(datos);

        Actividad actividad = buscarActividad(id);

        actividad.setDescripcion(datos.descripcion());
        actividad.setFecha(datos.fecha());
        actividad.setPonderacion(datos.ponderacion());

        return convertirAResponse(
                activityRepository.save(actividad));
    }

    public void eliminar(Long id, Long actorId) {

        validarPermiso(actorId);

        Actividad actividad = buscarActividad(id);

        try {
            activityRepository.delete(actividad);
            activityRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la actividad porque tiene calificaciones asociadas");
        }
    }

    private Actividad buscarActividad(Long id) {

        return activityRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "La actividad no existe"));
    }

    private void validarPermiso(Long actorId) {

        Usuario actor = usuarioRepository.findById(actorId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El usuario no existe"));

        if (!autorizacionService.tienePermiso(
                actor,
                Permiso.CREAR_ACTIVIDAD)) {

            throw new AccesoDenegadoException(
                    "El usuario no tiene permiso para gestionar actividades");
        }
    }

    private void validarActividad(ActividadRequest actividad) {

        if (actividad == null
                || actividad.descripcion() == null
                || actividad.descripcion().trim().isEmpty()
                || actividad.fecha() == null
                || actividad.ponderacion() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "descripcion, fecha y ponderacion son obligatorios");
        }
    }

    private ActividadResponse convertirAResponse(
            Actividad actividad) {

        return new ActividadResponse(
                actividad.getId(),
                actividad.getCurso().getId(),
                actividad.getDescripcion(),
                actividad.getFecha(),
                actividad.getPonderacion());
    }
}