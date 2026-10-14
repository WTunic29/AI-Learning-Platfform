package co.edu.unipiloto.ailearningmobile.dto;

public class ModuloProgresoResponse {

    private Long moduloId;
    private String nombre;
    private boolean completado;
    private String fechaCompletado;

    public Long getModuloId(){
        return moduloId;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isCompletado() {
        return completado;
    }

    public String getFechaCompletado() {
        return fechaCompletado;
    }

}
