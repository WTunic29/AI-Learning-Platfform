package co.edu.unipiloto.ailearningmobile.dto;

import java.math.BigDecimal;

public class CertificadoResponse {

    private Long id;
    private Long usuarioId;
    private String nombreEstudiante;
    private Long cursoId;
    private String nombreCurso;
    private String codigoVerificacion;
    private BigDecimal promedio;
    private String fechaEmision;

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }
}