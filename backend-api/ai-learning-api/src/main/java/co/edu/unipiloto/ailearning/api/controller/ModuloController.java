package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.ModuloCrudRequest;
import co.edu.unipiloto.ailearning.api.dto.ModuloCrudResponse;
import co.edu.unipiloto.ailearning.api.service.ModuloService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @GetMapping
    public ResponseEntity<List<ModuloCrudResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                moduloService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloCrudResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                moduloService.obtenerPorId(id)
        );
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ModuloCrudResponse>> obtenerPorCurso(
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(
                moduloService.obtenerPorCurso(cursoId)
        );
    }

    @PostMapping
    public ResponseEntity<ModuloCrudResponse> crear(
            @RequestBody ModuloCrudRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduloService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuloCrudResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ModuloCrudRequest request) {

        return ResponseEntity.ok(
                moduloService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        moduloService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}