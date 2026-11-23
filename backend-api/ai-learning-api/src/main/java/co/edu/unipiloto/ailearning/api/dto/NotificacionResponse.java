package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDateTime;

public record NotificacionResponse(
        Long id,
        Long usuarioId,
        String tipo,
        String mensaje,
        boolean leida,
        LocalDateTime fechaCreacion
) {
}