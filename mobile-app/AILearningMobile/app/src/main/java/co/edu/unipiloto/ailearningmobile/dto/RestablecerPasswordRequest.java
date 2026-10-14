package co.edu.unipiloto.ailearningmobile.dto;

public class RestablecerPasswordRequest {

    private String correo;
    private String codigo;
    private String nuevaPassword;

    public RestablecerPasswordRequest(
            String correo,
            String codigo,
            String nuevaPassword) {

        this.correo = correo;
        this.codigo = codigo;
        this.nuevaPassword = nuevaPassword;
    }
}