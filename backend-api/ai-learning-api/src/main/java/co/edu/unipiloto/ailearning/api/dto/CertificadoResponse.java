package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CertificadoResponse {

    private Long id;
    private Long usuarioId;
    private String nombreEstudiante;
    private Long cursoId;
    private String nombreCurso;
    private String codigoVerificacion;
    private BigDecimal promedio;
    private LocalDateTime fechaEmision;

    public CertificadoResponse(
            Long id,
            Long usuarioId,
            String nombreEstudiante,
            Long cursoId,
            String nombreCurso,
            String codigoVerificacion,
            BigDecimal promedio,
            LocalDateTime fechaEmision) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreEstudiante = nombreEstudiante;
        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.codigoVerificacion = codigoVerificacion;
        this.promedio = promedio;
        this.fechaEmision = fechaEmision;
    }

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

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
}