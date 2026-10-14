package co.edu.unipiloto.ailearning.api.dto;

public class SolicitarRecuperacionResponse {

    private String mensaje;
    private String codigoDemo;

    public SolicitarRecuperacionResponse(
            String mensaje,
            String codigoDemo) {

        this.mensaje = mensaje;
        this.codigoDemo = codigoDemo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getCodigoDemo() {
        return codigoDemo;
    }
}