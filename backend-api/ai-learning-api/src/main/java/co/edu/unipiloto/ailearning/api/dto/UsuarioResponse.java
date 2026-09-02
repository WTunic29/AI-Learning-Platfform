package co.edu.unipiloto.ailearning.api.dto;

import co.edu.unipiloto.ailearning.api.model.Usuario;

import java.time.LocalDateTime;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private LocalDateTime fechaRegistro;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.correo = usuario.getCorreo();
        this.rol = usuario.getRol();
        this.fechaRegistro = usuario.getFechaRegistro();
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}