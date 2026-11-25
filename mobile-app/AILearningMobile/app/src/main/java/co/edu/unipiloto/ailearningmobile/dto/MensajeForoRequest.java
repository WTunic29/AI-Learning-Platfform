package co.edu.unipiloto.ailearningmobile.dto;
public class MensajeForoRequest {
    private Long usuarioId;
    private String mensaje;
    public MensajeForoRequest(Long usuarioId, String mensaje){ this.usuarioId=usuarioId; this.mensaje=mensaje; }
    public Long getUsuarioId(){ return usuarioId; }
    public String getMensaje(){ return mensaje; }
}
