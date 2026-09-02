package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import co.edu.unipiloto.ailearning.api.dto.UsuarioResponse;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cambiarRol(Long id, String nuevoRol){

        //Buscar rol
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();
        //normalizar rol esto con el fin de evitar problemas con mayúsculas y minúsculas
        String rol = nuevoRol.toUpperCase().trim();
        //validamos los tres roles existentes actualmente
        if (!rol.equals("ESTUDIANTE") && !rol.equals("DOCENTE") && !rol.equals("ADMIN")) {

            throw new RuntimeException("Rol no valido");

        }

        //cambiar rol
        usuario.setRol(rol);
        //guardar cambios en postgres
        return  usuarioRepository.save(usuario);

    }
    public List<UsuarioResponse> buscarUsuarios(String termino) {

    if (termino == null || termino.trim().isEmpty()) {
        return List.of();
    }

    String busqueda = termino.trim();

    return usuarioRepository
            .findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                    busqueda,
                    busqueda
            )
            .stream()
            .map(UsuarioResponse::new)
            .toList();
}

}
