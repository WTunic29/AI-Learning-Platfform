package co.edu.unipiloto.ailearningmobile.dto;

import java.math.BigDecimal;

public class CalificacionResponse {

    private Long id;
    private Long actividadId;
    private Long estudianteId;
    private String nombreEstudiante;
    private BigDecimal nota;
    private String retroalimentacion;
    private String fechaCalificacion;

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

    public String getFechaCalificacion() {
        return fechaCalificacion;
    }
}