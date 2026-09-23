package co.edu.unipiloto.ailearning.api.dto;

import java.util.List;

public class ModuloResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private Integer orden;
    private List<RecursoResponse> recursos;

    public ModuloResponse(
            Long id,
            String titulo,
            String descripcion,
            Integer orden,
            List<RecursoResponse> recursos) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.orden = orden;
        this.recursos = recursos;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public List<RecursoResponse> getRecursos() {
        return recursos;
    }
}