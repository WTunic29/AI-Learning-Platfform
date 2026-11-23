package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.EntregaActividadResponse;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class EntregaActividadService {

    private final EntregaActividadRepository repository;
    private final ActivityRepository activityRepository;
    private final UsuarioRepository usuarioRepository;

    private final Path carpeta =
            Paths.get("uploads", "entregas")
                    .toAbsolutePath()
                    .normalize();

    public EntregaActividadService(
            EntregaActividadRepository repository,
            ActivityRepository activityRepository,
            UsuarioRepository usuarioRepository) {

        this.repository = repository;
        this.activityRepository = activityRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EntregaActividadResponse entregar(
            Long actividadId,
            Long estudianteId,
            MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe adjuntar un archivo"
            );
        }

        Actividad actividad = activityRepository
                .findById(actividadId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Actividad no encontrada"));

        Usuario estudiante = usuarioRepository
                .findById(estudianteId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Estudiante no encontrado"));

        try {
            Files.createDirectories(carpeta);

            String original = archivo.getOriginalFilename();

            if (original == null || original.isBlank()) {
                original = "entrega";
            }

            String seguro = original.replaceAll(
                    "[^a-zA-Z0-9._-]",
                    "_"
            );

            String nombreGuardado =
                    System.currentTimeMillis() +
                    "_" +
                    seguro;

            Path destino = carpeta
                    .resolve(nombreGuardado)
                    .normalize();

            if (!destino.startsWith(carpeta)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ruta de archivo no valida"
                );
            }

            Files.copy(
                    archivo.getInputStream(),
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );

            EntregaActividad entrega =
                    new EntregaActividad();

            entrega.setActividad(actividad);
            entrega.setEstudiante(estudiante);
            entrega.setNombreArchivo(original);
            entrega.setRutaArchivo(
                    destino.toString()
            );

            return toResponse(
                    repository.save(entrega)
            );

        } catch (IOException ex) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible almacenar el archivo"
            );
        }
    }

    public List<EntregaActividadResponse>
    listarPorActividad(Long actividadId) {

        return repository
                .findByActividadIdOrderByFechaEntregaDesc(
                        actividadId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private EntregaActividadResponse toResponse(
            EntregaActividad e) {

        return new EntregaActividadResponse(
                e.getId(),
                e.getActividad().getId(),
                e.getEstudiante().getId(),
                e.getEstudiante().getNombre(),
                e.getNombreArchivo(),
                e.getFechaEntrega()
        );
    }
}