package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.NotificacionResponse;
import co.edu.unipiloto.ailearning.api.service.NotificacionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponse>> obtener(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                service.obtenerPorUsuario(usuarioId)
        );
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<Map<String, Long>> contarNoLeidas(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                Map.of("noLeidas", service.contarNoLeidas(usuarioId))
        );
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponse> marcarLeida(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.marcarLeida(id)
        );
    }
}