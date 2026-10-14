package co.edu.unipiloto.ailearning.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_modulos", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "modulo_id"})})
public class ProgresoModulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @Column(name = "fecha_completado", nullable = false)
    private LocalDateTime fechaCompletado;

    public ProgresoModulo(){
    }

    @PrePersist
    protected void onCreate(){

        if (fechaCompletado == null){
            fechaCompletado = LocalDateTime.now();
        }

    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

}
