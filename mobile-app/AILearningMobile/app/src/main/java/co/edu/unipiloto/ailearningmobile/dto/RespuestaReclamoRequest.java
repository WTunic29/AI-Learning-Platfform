package co.edu.unipiloto.ailearningmobile.dto;
public class RespuestaReclamoRequest {
    private String respuesta, estado;
    public RespuestaReclamoRequest(String respuesta,String estado){this.respuesta=respuesta;this.estado=estado;}
    public String getRespuesta(){return respuesta;} public String getEstado(){return estado;}
}
