package co.edu.unipiloto.ailearningmobile.dto;

public class EspacioEducativoCercanoResponse {

    private Long id;
    private String nombre;
    private String tipo;
    private String direccion;
    private String descripcion;
    private double latitud;
    private double longitud;
    private double distanciaKm;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getDireccion() { return direccion; }
    public String getDescripcion() { return descripcion; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public double getDistanciaKm() { return distanciaKm; }
}