package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDateTime;

public class InscripcionResponse {

    private Long id;
    private Long usuarioId;
    private Long cursoId;
    private String nombreCurso;
    private LocalDateTime fechaInscripcion;

    public InscripcionResponse(){
    }

    public InscripcionResponse(
            Long id, Long usuarioId, Long cursoId,
            String nombreCurso, LocalDateTime fechaInscripcion){

        this.id = id;
        this.usuarioId = usuarioId;
        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.fechaInscripcion = fechaInscripcion;

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

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }
}
