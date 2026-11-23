package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDateTime;

public record ReclamoResponse(
        Long id,
        Long estudianteId,
        String estudianteNombre,
        Long actividadId,
        String motivo,
        String estado,
        String respuesta,
        LocalDateTime fechaCreacion
) {
}