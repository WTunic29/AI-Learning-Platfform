package co.edu.unipiloto.ailearningmobile.dto;

public class RegisterRequest {

    private String nombre;
    private String correo;
    private String password;

    public RegisterRequest(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }
}