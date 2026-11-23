package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.*;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActivityRepository activityRepository;

    public ReclamoService(
            ReclamoRepository reclamoRepository,
            UsuarioRepository usuarioRepository,
            ActivityRepository activityRepository) {

        this.reclamoRepository = reclamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.activityRepository = activityRepository;
    }

    public ReclamoResponse crear(ReclamoRequest request) {

        Usuario estudiante = usuarioRepository
                .findById(request.estudianteId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Estudiante no encontrado"));

        Reclamo reclamo = new Reclamo();
        reclamo.setEstudiante(estudiante);
        reclamo.setMotivo(request.motivo());
        reclamo.setEstado("ABIERTO");

        if (request.actividadId() != null) {
            Actividad actividad = activityRepository
                    .findById(request.actividadId())
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Actividad no encontrada"));

            reclamo.setActividad(actividad);
        }

        return toResponse(
                reclamoRepository.save(reclamo)
        );
    }

    public List<ReclamoResponse> listar() {
        return reclamoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReclamoResponse> listarPorEstudiante(
            Long estudianteId) {

        return reclamoRepository
                .findByEstudianteIdOrderByFechaCreacionDesc(
                        estudianteId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReclamoResponse responder(
            Long id,
            ReclamoRespuestaRequest request) {

        Reclamo reclamo = reclamoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Reclamo no encontrado"));

        reclamo.setRespuesta(request.respuesta());

        if (request.estado() != null &&
                !request.estado().isBlank()) {
            reclamo.setEstado(request.estado());
        } else {
            reclamo.setEstado("RESUELTO");
        }

        return toResponse(
                reclamoRepository.save(reclamo)
        );
    }

    private ReclamoResponse toResponse(Reclamo r) {

        Long actividadId = r.getActividad() == null
                ? null
                : r.getActividad().getId();

        return new ReclamoResponse(
                r.getId(),
                r.getEstudiante().getId(),
                r.getEstudiante().getNombre(),
                actividadId,
                r.getMotivo(),
                r.getEstado(),
                r.getRespuesta(),
                r.getFechaCreacion()
        );
    }
}