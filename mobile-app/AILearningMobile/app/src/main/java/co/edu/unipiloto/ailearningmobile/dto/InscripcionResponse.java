package co.edu.unipiloto.ailearningmobile.dto;

public class InscripcionResponse {

    private Long id;
    private Long usuarioId;
    private Long cursoId;
    private String nombreCurso;
    private String fechaInscripcion;

    public InscripcionResponse(){
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

}
