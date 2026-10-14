package co.edu.unipiloto.ailearning.api.dto;

import java.util.List;

public class ProgresoCursoResponse {

    private Long cursoId;
    private String nombreCurso;
    private int totalModulos;
    private int modulosCompletados;
    private int modulosPendientes;
    private double porcentajeAvance;
    private List<ModuloProgresoResponse> modulos;

    public ProgresoCursoResponse() {
    }

    public ProgresoCursoResponse(Long cursoId, String nombreCurso, int totalModulos, int modulosCompletados, int modulosPendientes,
            double porcentajeAvance, List<ModuloProgresoResponse> modulos) {

        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.totalModulos = totalModulos;
        this.modulosCompletados = modulosCompletados;
        this.modulosPendientes = modulosPendientes;
        this.porcentajeAvance = porcentajeAvance;
        this.modulos = modulos;

    }

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
