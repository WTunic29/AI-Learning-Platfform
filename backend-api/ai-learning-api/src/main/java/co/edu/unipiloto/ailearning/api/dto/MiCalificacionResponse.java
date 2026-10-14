package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MiCalificacionResponse {

    private Long actividadId;
    private String actividad;
    private BigDecimal nota;
    private BigDecimal ponderacion;
    private String retroalimentacion;
    private LocalDateTime fechaCalificacion;

    public MiCalificacionResponse(
            Long actividadId,
            String actividad,
            BigDecimal nota,
            BigDecimal ponderacion,
            String retroalimentacion,
            LocalDateTime fechaCalificacion) {

        this.actividadId = actividadId;
        this.actividad = actividad;
        this.nota = nota;
        this.ponderacion = ponderacion;
        this.retroalimentacion = retroalimentacion;
        this.fechaCalificacion = fechaCalificacion;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public String getActividad() {
        return actividad;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public BigDecimal getPonderacion() {
        return ponderacion;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }
}