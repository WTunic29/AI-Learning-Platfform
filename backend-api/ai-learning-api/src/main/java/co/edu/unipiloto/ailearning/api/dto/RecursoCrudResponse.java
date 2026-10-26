package co.edu.unipiloto.ailearning.api.dto;

public class RecursoCrudResponse {

    private Long id;
    private Long moduloId;
    private String tituloModulo;
    private String titulo;
    private String tipo;
    private String url;
    private Integer orden;

    public RecursoCrudResponse(
            Long id,
            Long moduloId,
            String tituloModulo,
            String titulo,
            String tipo,
            String url,
            Integer orden) {

        this.id = id;
        this.moduloId = moduloId;
        this.tituloModulo = tituloModulo;
        this.titulo = titulo;
        this.tipo = tipo;
        this.url = url;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public String getTituloModulo() {
        return tituloModulo;
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