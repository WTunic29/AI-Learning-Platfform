package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.ModuloResponse;
import co.edu.unipiloto.ailearning.api.service.ContenidoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenidos")
public class ContenidoController {

    private final ContenidoService contenidoService;

    public ContenidoController(
            ContenidoService contenidoService) {

        this.contenidoService = contenidoService;
    }

    @GetMapping("/usuario/{usuarioId}/curso/{cursoId}")
    public ResponseEntity<List<ModuloResponse>> obtenerContenido(
            @PathVariable Long usuarioId,
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(
                contenidoService.obtenerContenido(
                        usuarioId,
                        cursoId
                )
        );
    }
}