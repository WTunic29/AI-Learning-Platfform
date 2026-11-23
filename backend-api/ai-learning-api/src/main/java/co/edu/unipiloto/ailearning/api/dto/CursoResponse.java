package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDate;

public class CursoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String objetivo;
    private String resultadosEsperados;
    private Integer cupoMaximo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long docenteId;
    private String docenteNombre;

    public CursoResponse() {
    }

    // Se mantiene por compatibilidad con codigo existente
    public CursoResponse(Long id, String nombre, String descripcion, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public CursoResponse(
            Long id,
            String nombre,
            String descripcion,
            String categoria,
            String objetivo,
            String resultadosEsperados,
            Integer cupoMaximo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Long docenteId,
            String docenteNombre) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.objetivo = objetivo;
        this.resultadosEsperados = resultadosEsperados;
        this.cupoMaximo = cupoMaximo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.docenteId = docenteId;
        this.docenteNombre = docenteNombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getResultadosEsperados() { return resultadosEsperados; }
    public void setResultadosEsperados(String resultadosEsperados) { this.resultadosEsperados = resultadosEsperados; }

    public Integer getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }
}