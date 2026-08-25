package co.edu.unipiloto.ailearning.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;

// entity le dice a JPA que esta clase sera una entidad que quiero persistir en la DB
@Entity
@Table(name = "usuarios")
public class Usuario {

    //representa a usuarios.id de la db, tambien genera automaticamente el siguiente id gracias al BIGSERIAL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Corresponde a nombre de la DB y dice que no puede estar vacio, y tiene un minimo de 100 caracteres
    @Column(nullable = false, length = 100)
    private String nombre;

    //el unique representa a que no pueden haber dos usuarios con el mismo correo
    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    // aqui se nombra el password_hash ya que en postgreSQL usamos password_hash... mas adelante usaremos BCrypt pa generar el hash
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // en un principio tendremos Estudiante, docente, admin. Pero, aun no necesitamos implementar el sistema de roles completo
    @Column(nullable = false, length = 30)
    private String rol;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate(){
        fechaRegistro = LocalDateTime.now();
    }

    public Usuario(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
