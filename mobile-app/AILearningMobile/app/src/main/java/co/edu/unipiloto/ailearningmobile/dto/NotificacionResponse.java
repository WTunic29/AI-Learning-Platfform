package co.edu.unipiloto.ailearningmobile.dto;

public class NotificacionResponse {
    private Long id;
    private Long usuarioId;
    private String tipo;
    private String mensaje;
    private boolean leida;
    private String fechaCreacion;
    public Long getId(){ return id; }
    public Long getUsuarioId(){ return usuarioId; }
    public String getTipo(){ return tipo; }
    public String getMensaje(){ return mensaje; }
    public boolean isLeida(){ return leida; }
    public String getFechaCreacion(){ return fechaCreacion; }
}
