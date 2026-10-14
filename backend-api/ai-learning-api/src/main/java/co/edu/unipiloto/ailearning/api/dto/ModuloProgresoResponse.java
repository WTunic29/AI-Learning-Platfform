package co.edu.unipiloto.ailearning.api.dto;

import java.time.LocalDateTime;

public class ModuloProgresoResponse {

    private Long moduloId;
    private String nombre;
    private boolean completado;
    private LocalDateTime fechaCompletado;

    public ModuloProgresoResponse() {
    }

    public ModuloProgresoResponse(Long moduloId, String nombre, boolean completado, LocalDateTime fechaCompletado) {

        this.moduloId = moduloId;
        this.nombre = nombre;
        this.completado = completado;
        this.fechaCompletado = fechaCompletado;

    }

    public Long getModuloId() {
        return moduloId;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isCompletado() {
        return completado;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

}
