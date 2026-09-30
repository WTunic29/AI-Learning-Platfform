package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CalificacionResponse {

    private Long id;
    private Long actividadId;
    private Long estudianteId;
    private String nombreEstudiante;
    private BigDecimal nota;
    private String retroalimentacion;
    private LocalDateTime fechaCalificacion;

    public CalificacionResponse(
            Long id,
            Long actividadId,
            Long estudianteId,
            String nombreEstudiante,
            BigDecimal nota,
            String retroalimentacion,
            LocalDateTime fechaCalificacion) {

        this.id = id;
        this.actividadId = actividadId;
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.nota = nota;
        this.retroalimentacion = retroalimentacion;
        this.fechaCalificacion = fechaCalificacion;
    }

    public Long getId() {
        return id;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }
}