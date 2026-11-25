package co.edu.unipiloto.ailearningmobile.dto;
public class EntregaActividadResponse {
    private Long id, actividadId, estudianteId;
    private String estudianteNombre, nombreArchivo, fechaEntrega;
    public Long getId(){return id;} public Long getActividadId(){return actividadId;} public Long getEstudianteId(){return estudianteId;}
    public String getEstudianteNombre(){return estudianteNombre;} public String getNombreArchivo(){return nombreArchivo;} public String getFechaEntrega(){return fechaEntrega;}
}
