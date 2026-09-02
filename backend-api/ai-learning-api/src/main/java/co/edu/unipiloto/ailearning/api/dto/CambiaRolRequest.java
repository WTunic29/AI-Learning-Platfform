package co.edu.unipiloto.ailearning.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CambiaRolRequest {

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    public CambiaRolRequest() {

    }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol){
            this.rol = rol;

        }

}
