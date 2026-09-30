package co.edu.unipiloto.ailearningmobile.dto;

import java.math.BigDecimal;
public class CrearActividadRequest {
    private String descripcion;
    private String fecha;
    private BigDecimal ponderacion;

    public CrearActividadRequest() {
    }

    public CrearActividadRequest(String descripcion, String fecha, BigDecimal ponderacion) {

        this.descripcion = descripcion;
        this.fecha = fecha;
        this.ponderacion = ponderacion;

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(BigDecimal ponderacion) {
        this.ponderacion = ponderacion;
    }

}
