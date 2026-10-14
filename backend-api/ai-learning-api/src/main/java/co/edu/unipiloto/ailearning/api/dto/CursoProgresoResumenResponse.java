package co.edu.unipiloto.ailearning.api.dto;

public class CursoProgresoResumenResponse {

    private Long cursoId;
    private String nombreCurso;
    private double porcentajeAvance;
    private String estado;

    public CursoProgresoResumenResponse(
            Long cursoId,
            String nombreCurso,
            double porcentajeAvance,
            String estado) {

        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.porcentajeAvance = porcentajeAvance;
        this.estado = estado;
    }

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