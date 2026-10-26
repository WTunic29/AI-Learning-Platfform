package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.ActividadResponse;
import co.edu.unipiloto.ailearning.api.model.Actividad;
import co.edu.unipiloto.ailearning.api.service.ActividadService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(
            ActividadService actividadService) {

        this.actividadService = actividadService;
    }

    @PostMapping("/curso/{cursoId}")
    public ResponseEntity<ActividadResponse> crearActividad(
            @PathVariable Long cursoId,
            @RequestParam Long actorId,
            @RequestBody Actividad actividad) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadService.crearActividad(
                        cursoId,
                        actividad,
                        actorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                actividadService.obtenerPorId(id));
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ActividadResponse>> obtenerActividades(
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(
                actividadService.obtenerActividades(cursoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponse> actualizar(
            @PathVariable Long id,
            @RequestParam Long actorId,
            @RequestBody Actividad actividad) {

        return ResponseEntity.ok(
                actividadService.actualizar(
                        id,
                        actividad,
                        actorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam Long actorId) {

        actividadService.eliminar(id, actorId);

        return ResponseEntity.noContent().build();
    }
}