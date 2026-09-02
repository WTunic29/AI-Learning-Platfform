package co.edu.unipiloto.ailearningmobile.dto;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private String fechaRegistro;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }
}
