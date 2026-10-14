package co.edu.unipiloto.ailearningmobile.dto;

public class SolicitarRecuperacionRequest {

    private String correo;

    public SolicitarRecuperacionRequest(
            String correo) {

        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }
}