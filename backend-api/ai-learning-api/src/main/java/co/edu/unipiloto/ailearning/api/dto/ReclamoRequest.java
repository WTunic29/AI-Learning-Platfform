package co.edu.unipiloto.ailearning.api.dto;

public record ReclamoRequest(
        Long estudianteId,
        Long actividadId,
        String motivo
) {
}