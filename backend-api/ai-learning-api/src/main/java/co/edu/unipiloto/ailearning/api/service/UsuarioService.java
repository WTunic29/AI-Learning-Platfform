package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;
import co.edu.unipiloto.ailearning.api.model.Rol;
import co.edu.unipiloto.ailearning.api.dto.UsuarioResponse;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.Permiso;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AutorizacionService autorizacionService;

    public UsuarioService(UsuarioRepository usuarioRepository, AutorizacionService autorizacionService){

        this.usuarioRepository = usuarioRepository;
        this.autorizacionService = autorizacionService;

    }

    public Usuario cambiarRol(Long id, String nuevoRol, Long actorId){

        Usuario actor = usuarioRepository.findById(actorId).orElseThrow(() ->
                        new RuntimeException("El usuario que realiza la operación no existe"));

        if (!autorizacionService.tienePermiso(actor, Permiso.GESTIONAR_ROLES)) {
            throw new AccesoDenegadoException("El usuario no tiene permiso para gestionar roles");
        }

        //Buscar rol
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        //normalizar rol esto con el fin de evitar problemas con mayúsculas y minúsculas
        String rolNormalizado = nuevoRol.toUpperCase().trim();

        Rol rol;

        try {
            rol = Rol.valueOf(rolNormalizado);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido");
        }

        usuario.setRol(rol);
        //guardar cambios en postgres
        return  usuarioRepository.save(usuario);

    }
    public List<UsuarioResponse> buscarUsuarios(String termino) {

    if (termino == null || termino.trim().isEmpty()) {
        return List.of();
    }

    String busqueda = termino.trim();

    return usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                    busqueda, busqueda).stream().map(UsuarioResponse::new).toList();

    }

}
