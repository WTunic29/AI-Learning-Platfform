package co.edu.unipiloto.ailearningmobile.dto;
public class MensajeForoResponse {
    private Long id, cursoId, usuarioId;
    private String usuarioNombre, mensaje, fechaCreacion;
    public Long getId(){return id;} public Long getCursoId(){return cursoId;} public Long getUsuarioId(){return usuarioId;}
    public String getUsuarioNombre(){return usuarioNombre;} public String getMensaje(){return mensaje;} public String getFechaCreacion(){return fechaCreacion;}
}
