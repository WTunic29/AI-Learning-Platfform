package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.ActividadResponse;
import co.edu.unipiloto.ailearning.api.model.Actividad;
import co.edu.unipiloto.ailearning.api.service.ActividadService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @PostMapping("/curso/{cursoId}")
    public ResponseEntity<ActividadResponse> crearActividad(@PathVariable Long cursoId, @RequestParam Long actorId, @RequestBody Actividad actividad) {
        ActividadResponse respuesta = actividadService.crearActividad(cursoId, actividad, actorId);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ActividadResponse>> obtenerActividades(@PathVariable Long cursoId) {
        return ResponseEntity.ok(actividadService.obtenerActividades(cursoId));
    }

}
