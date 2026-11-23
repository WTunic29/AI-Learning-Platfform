package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.EntregaActividadResponse;
import co.edu.unipiloto.ailearning.api.service.EntregaActividadService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaActividadController {

    private final EntregaActividadService service;

    public EntregaActividadController(
            EntregaActividadService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/actividad/{actividadId}/estudiante/{estudianteId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EntregaActividadResponse> entregar(
            @PathVariable Long actividadId,
            @PathVariable Long estudianteId,
            @RequestPart("archivo") MultipartFile archivo) {

        return ResponseEntity.ok(
                service.entregar(
                        actividadId,
                        estudianteId,
                        archivo
                )
        );
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<EntregaActividadResponse>>
    listarPorActividad(
            @PathVariable Long actividadId) {

        return ResponseEntity.ok(
                service.listarPorActividad(
                        actividadId
                )
        );
    }
}