package co.edu.unipiloto.ailearning.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "espacios_educativos")
public class EspacioEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 255)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, precision = 10, scale = 7)
    private java.math.BigDecimal latitud;

    @Column(nullable = false, precision = 10, scale = 7)
    private java.math.BigDecimal longitud;

    @Column(nullable = false)
    private Boolean activo = true;

    public EspacioEducativo() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public java.math.BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(java.math.BigDecimal latitud) {
        this.latitud = latitud;
    }

    public java.math.BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(java.math.BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}
