package co.edu.unipiloto.ailearningmobile.dto;

import java.util.List;
public class ProgresoCursoResponse {

    private Long cursoId;
    private String nombreCurso;
    private int totalModulos;
    private int modulosCompletados;
    private int modulosPendientes;
    private double porcentajeAvance;
    private List<ModuloProgresoResponse> modulos;

    public Long getCursoId() {
        return cursoId;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public int getTotalModulos() {
        return totalModulos;
    }

    public int getModulosCompletados() {
        return modulosCompletados;
    }

    public int getModulosPendientes() {
        return modulosPendientes;
    }

    public double getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public List<ModuloProgresoResponse> getModulos() {
        return modulos;
    }

}
