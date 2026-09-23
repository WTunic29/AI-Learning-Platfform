package co.edu.unipiloto.ailearningmobile.dto;

import java.util.List;

public class ModuloResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private Integer orden;
    private List<RecursoResponse> recursos;

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