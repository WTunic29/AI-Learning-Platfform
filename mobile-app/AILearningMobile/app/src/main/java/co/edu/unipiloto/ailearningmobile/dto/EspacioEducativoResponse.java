package co.edu.unipiloto.ailearningmobile.dto;

public class EspacioEducativoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String tipo;
    private double latitud;
    private double longitud;
    private Boolean activo;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public Boolean getActivo() {
        return activo;
    }

}
