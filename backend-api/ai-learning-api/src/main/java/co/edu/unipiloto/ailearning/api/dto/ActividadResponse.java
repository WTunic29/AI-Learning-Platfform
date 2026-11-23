package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ActividadResponse {

    private Long id;
    private Long cursoId;
    private String descripcion;
    private LocalDate fecha;
    private BigDecimal ponderacion;
    private String tipo;
    private LocalDate fechaApertura;
    private LocalDate fechaCierre;

    public ActividadResponse() {
    }

    public ActividadResponse(
            Long id,
            Long cursoId,
            String descripcion,
            LocalDate fecha,
            BigDecimal ponderacion,
            String tipo,
            LocalDate fechaApertura,
            LocalDate fechaCierre) {

        this.id = id;
        this.cursoId = cursoId;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.ponderacion = ponderacion;
        this.tipo = tipo;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
    }

    public Long getId() { return id; }
    public Long getCursoId() { return cursoId; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getPonderacion() { return ponderacion; }
    public String getTipo() { return tipo; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public LocalDate getFechaCierre() { return fechaCierre; }
}