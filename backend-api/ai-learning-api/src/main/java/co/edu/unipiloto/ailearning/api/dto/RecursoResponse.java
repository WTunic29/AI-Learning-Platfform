package co.edu.unipiloto.ailearning.api.dto;

public class RecursoResponse {

    private Long id;
    private String titulo;
    private String tipo;
    private String url;
    private Integer orden;

    public RecursoResponse(
            Long id,
            String titulo,
            String tipo,
            String url,
            Integer orden) {

        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.url = url;
        this.orden = orden;
    }

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