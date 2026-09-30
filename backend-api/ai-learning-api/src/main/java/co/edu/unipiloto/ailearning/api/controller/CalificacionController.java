package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.CalificacionResponse;
import co.edu.unipiloto.ailearning.api.dto.EstudianteCursoResponse;
import co.edu.unipiloto.ailearning.api.dto.RegistrarCalificacionRequest;
import co.edu.unipiloto.ailearning.api.service.CalificacionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(
            CalificacionService calificacionService) {

        this.calificacionService = calificacionService;
    }

    @PostMapping("/actividad/{actividadId}")
    public ResponseEntity<CalificacionResponse> registrar(
            @PathVariable Long actividadId,
            @RequestParam Long actorId,
            @RequestBody RegistrarCalificacionRequest request) {

        return ResponseEntity.ok(
                calificacionService.registrar(
                        actividadId,
                        actorId,
                        request
                )
        );
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<CalificacionResponse>>
    obtenerPorActividad(
            @PathVariable Long actividadId) {

        return ResponseEntity.ok(
                calificacionService
                        .obtenerPorActividad(actividadId)
        );
    }

    @GetMapping("/curso/{cursoId}/estudiantes")
    public ResponseEntity<List<EstudianteCursoResponse>>
    obtenerEstudiantesCurso(
            @PathVariable Long cursoId,
            @RequestParam Long actorId) {

        return ResponseEntity.ok(
                calificacionService.obtenerEstudiantesCurso(
                        cursoId,
                        actorId
                )
        );
    }
}