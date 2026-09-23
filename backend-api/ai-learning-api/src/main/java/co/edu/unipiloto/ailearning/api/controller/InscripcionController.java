package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.InscripcionResponse;
import co.edu.unipiloto.ailearning.api.model.Inscripcion;
import co.edu.unipiloto.ailearning.api.service.InscripcionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<InscripcionResponse> inscribir(@PathVariable Long usuarioId, @PathVariable Long cursoId) {

        InscripcionResponse respuesta = inscripcionService.inscribir(usuarioId, cursoId);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InscripcionResponse>> obtenerInscripciones(@PathVariable Long usuarioId) {

        List<InscripcionResponse> inscripciones = inscripcionService.obtenerInscripciones(usuarioId);
        return ResponseEntity.ok(inscripciones);
    }
}
