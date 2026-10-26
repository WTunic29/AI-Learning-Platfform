package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.RecursoCrudRequest;
import co.edu.unipiloto.ailearning.api.dto.RecursoCrudResponse;
import co.edu.unipiloto.ailearning.api.service.RecursoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(
            RecursoService recursoService) {

        this.recursoService = recursoService;
    }

    @GetMapping
    public ResponseEntity<List<RecursoCrudResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                recursoService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoCrudResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                recursoService.obtenerPorId(id)
        );
    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<RecursoCrudResponse>> obtenerPorModulo(
            @PathVariable Long moduloId) {

        return ResponseEntity.ok(
                recursoService.obtenerPorModulo(moduloId)
        );
    }

    @PostMapping
    public ResponseEntity<RecursoCrudResponse> crear(
            @RequestBody RecursoCrudRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recursoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoCrudResponse> actualizar(
            @PathVariable Long id,
            @RequestBody RecursoCrudRequest request) {

        return ResponseEntity.ok(
                recursoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        recursoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}