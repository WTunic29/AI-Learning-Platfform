package co.edu.unipiloto.ailearningmobile.dto;
public class ReclamoRequest {
    private Long estudianteId, actividadId;
    private String motivo;
    public ReclamoRequest(Long estudianteId, Long actividadId, String motivo){this.estudianteId=estudianteId;this.actividadId=actividadId;this.motivo=motivo;}
    public Long getEstudianteId(){return estudianteId;} public Long getActividadId(){return actividadId;} public String getMotivo(){return motivo;}
}
