package co.edu.unipiloto.ailearningmobile.dto;

public class CursoProgresoResumenResponse {

    private Long cursoId;
    private String nombreCurso;
    private double porcentajeAvance;
    private String estado;

    public Long getCursoId() {
        return cursoId;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public double getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public String getEstado() {
        return estado;
    }
}