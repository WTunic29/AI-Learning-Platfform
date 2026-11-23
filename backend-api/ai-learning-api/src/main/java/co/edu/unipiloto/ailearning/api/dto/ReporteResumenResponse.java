package co.edu.unipiloto.ailearning.api.dto;

public record ReporteResumenResponse(
        long usuarios,
        long cursos,
        long inscripciones,
        long actividades,
        long certificados
) {
}