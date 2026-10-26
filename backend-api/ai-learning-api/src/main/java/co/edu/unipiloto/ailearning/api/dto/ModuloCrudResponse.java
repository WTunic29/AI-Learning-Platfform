package co.edu.unipiloto.ailearning.api.dto;

public class ModuloCrudResponse {

    private Long id;
    private Long cursoId;
    private String nombreCurso;
    private String titulo;
    private String descripcion;
    private Integer orden;

    public ModuloCrudResponse(
            Long id,
            Long cursoId,
            String nombreCurso,
            String titulo,
            String descripcion,
            Integer orden) {

        this.id = id;
        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public String getNombreCurso() {
        return nombreCurso;
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
}