package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDateTime;

public record EntregaActividadResponse(
        Long id,
        Long actividadId,
        Long estudianteId,
        String estudianteNombre,
        String nombreArchivo,
        LocalDateTime fechaEntrega
) {
}