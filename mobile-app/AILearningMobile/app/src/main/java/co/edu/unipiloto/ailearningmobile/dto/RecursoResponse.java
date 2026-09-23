package co.edu.unipiloto.ailearningmobile.dto;

public class RecursoResponse {

    private Long id;
    private String titulo;
    private String tipo;
    private String url;
    private Integer orden;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUrl() {
        return url;
    }

    public Integer getOrden() {
        return orden;
    }
}