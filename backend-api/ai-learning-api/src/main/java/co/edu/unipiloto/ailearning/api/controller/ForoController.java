package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.*;
import co.edu.unipiloto.ailearning.api.service.ForoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foros")
public class ForoController {

    private final ForoService service;

    public ForoController(ForoService service) {
        this.service = service;
    }

    @PostMapping("/curso/{cursoId}")
    public ResponseEntity<MensajeForoResponse> publicar(
            @PathVariable Long cursoId,
            @RequestBody MensajeForoRequest request) {

        return ResponseEntity.ok(
                service.publicar(cursoId, request)
        );
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<MensajeForoResponse>> listar(
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(
                service.listar(cursoId)
        );
    }
}