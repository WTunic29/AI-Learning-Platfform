package co.edu.unipiloto.ailearningmobile.dto;

import java.math.BigDecimal;

public class MiCalificacionResponse {

    private Long actividadId;
    private String actividad;
    private BigDecimal nota;
    private BigDecimal ponderacion;
    private String retroalimentacion;
    private String fechaCalificacion;

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

    public String getFechaCalificacion() {
        return fechaCalificacion;
    }
}