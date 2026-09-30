package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ActividadResponse {

    private Long id;
    private Long cursoId;
    private String descripcion;
    private LocalDate fecha;
    private BigDecimal ponderacion;

    public ActividadResponse() {
    }

    public ActividadResponse(Long id, Long cursoId, String descripcion, LocalDate fecha, BigDecimal ponderacion) {

        this.id = id;
        this.cursoId = cursoId;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.ponderacion = ponderacion;

    }

    public Long getId() {
        return id;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getPonderacion() {
        return ponderacion;
    }

}
