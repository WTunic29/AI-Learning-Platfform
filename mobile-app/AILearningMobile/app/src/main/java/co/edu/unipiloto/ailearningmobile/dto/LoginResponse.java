package co.edu.unipiloto.ailearningmobile.dto;

public class LoginResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;

    public LoginResponse() {
    }

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
}