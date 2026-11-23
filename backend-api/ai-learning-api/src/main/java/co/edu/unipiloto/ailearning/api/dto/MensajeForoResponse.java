package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDateTime;

public record MensajeForoResponse(
        Long id,
        Long cursoId,
        Long usuarioId,
        String usuarioNombre,
        String mensaje,
        LocalDateTime fechaCreacion
) {
}