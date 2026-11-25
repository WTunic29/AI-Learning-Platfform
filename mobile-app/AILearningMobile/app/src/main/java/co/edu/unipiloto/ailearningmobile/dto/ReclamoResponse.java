package co.edu.unipiloto.ailearningmobile.dto;
public class ReclamoResponse {
    private Long id, estudianteId, actividadId;
    private String estudianteNombre, motivo, estado, respuesta, fechaCreacion;
    public Long getId(){return id;} public Long getEstudianteId(){return estudianteId;} public Long getActividadId(){return actividadId;}
    public String getEstudianteNombre(){return estudianteNombre;} public String getMotivo(){return motivo;} public String getEstado(){return estado;}
    public String getRespuesta(){return respuesta;} public String getFechaCreacion(){return fechaCreacion;}
}
