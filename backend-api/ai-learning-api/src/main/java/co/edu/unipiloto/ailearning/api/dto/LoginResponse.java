package co.edu.unipiloto.ailearning.api.dto;

//Necesitamos separar  informacion interna de la DB de la que exponemos mediante la API
public class LoginResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;

    public LoginResponse(Long id, String nombre, String correo, String rol){

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;

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
