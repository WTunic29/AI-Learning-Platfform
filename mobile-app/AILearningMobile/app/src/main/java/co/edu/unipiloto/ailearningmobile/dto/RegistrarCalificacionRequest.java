package co.edu.unipiloto.ailearningmobile.dto;

import java.math.BigDecimal;

public class RegistrarCalificacionRequest {

    private Long estudianteId;
    private BigDecimal nota;
    private String retroalimentacion;

    public RegistrarCalificacionRequest(
            Long estudianteId,
            BigDecimal nota,
            String retroalimentacion) {

        this.estudianteId = estudianteId;
        this.nota = nota;
        this.retroalimentacion = retroalimentacion;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }
}