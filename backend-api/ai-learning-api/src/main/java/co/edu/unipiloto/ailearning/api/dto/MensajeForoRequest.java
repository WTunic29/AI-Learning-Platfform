package co.edu.unipiloto.ailearning.api.dto;

public record MensajeForoRequest(
        Long usuarioId,
        String mensaje
) {
}