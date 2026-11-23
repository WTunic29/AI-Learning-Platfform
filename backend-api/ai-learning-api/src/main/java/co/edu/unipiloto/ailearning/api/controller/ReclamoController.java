package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.*;
import co.edu.unipiloto.ailearning.api.service.ReclamoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reclamos")
public class ReclamoController {

    private final ReclamoService service;

    public ReclamoController(ReclamoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReclamoResponse> crear(
            @RequestBody ReclamoRequest request) {

        return ResponseEntity.ok(
                service.crear(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<ReclamoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<ReclamoResponse>> porEstudiante(
            @PathVariable Long estudianteId) {

        return ResponseEntity.ok(
                service.listarPorEstudiante(estudianteId)
        );
    }

    @PutMapping("/{id}/respuesta")
    public ResponseEntity<ReclamoResponse> responder(
            @PathVariable Long id,
            @RequestBody ReclamoRespuestaRequest request) {

        return ResponseEntity.ok(
                service.responder(id, request)
        );
    }
}