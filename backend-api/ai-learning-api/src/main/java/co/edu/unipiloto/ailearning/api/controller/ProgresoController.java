package co.edu.unipiloto.ailearning.api.controller;

import java.util.List;

import co.edu.unipiloto.ailearning.api.dto.CursoProgresoResumenResponse;
import co.edu.unipiloto.ailearning.api.dto.ProgresoCursoResponse;
import co.edu.unipiloto.ailearning.api.service.ProgresoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progreso")
public class ProgresoController {

    private final ProgresoService progresoService;

    public ProgresoController(ProgresoService progresoService) {
        this.progresoService = progresoService;
    }

    @PostMapping("/usuario/{usuarioId}/modulo/{moduloId}")
    public ResponseEntity<Void> marcarModuloCompletado(@PathVariable Long usuarioId, @PathVariable Long moduloId) {

        progresoService.marcarModuloCompletado(usuarioId, moduloId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/usuario/{usuarioId}/curso/{cursoId}")
    public ResponseEntity<ProgresoCursoResponse> obtenerProgresoCurso(@PathVariable Long usuarioId, @PathVariable Long cursoId) {
        return ResponseEntity.ok(progresoService.obtenerProgresoCurso(usuarioId, cursoId));
    }

    @GetMapping("/usuario/{usuarioId}/cursos")
    public ResponseEntity<List<CursoProgresoResumenResponse>> obtenerMisCursos(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                progresoService.obtenerMisCursos(usuarioId)
        );
    }

}