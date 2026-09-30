package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;

public class RegistrarCalificacionRequest {

    private Long estudianteId;
    private BigDecimal nota;
    private String retroalimentacion;

    public RegistrarCalificacionRequest() {
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public void setRetroalimentacion(String retroalimentacion) {
        this.retroalimentacion = retroalimentacion;
    }
}