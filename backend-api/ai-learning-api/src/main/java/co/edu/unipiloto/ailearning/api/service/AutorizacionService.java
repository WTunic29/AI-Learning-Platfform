package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Permiso;
import co.edu.unipiloto.ailearning.api.model.Rol;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.model.UsuarioPermiso;
import co.edu.unipiloto.ailearning.api.repository.UsuarioPermisoRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class AutorizacionService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioPermisoRepository usuarioPermisoRepository;
    private final RolPermisosService rolPermisosService;

    public AutorizacionService(UsuarioRepository usuarioRepository, UsuarioPermisoRepository usuarioPermisoRepository,
                               RolPermisosService rolPermisosService) {

        this.usuarioRepository = usuarioRepository;
        this.usuarioPermisoRepository = usuarioPermisoRepository;
        this.rolPermisosService = rolPermisosService;

    }

    public boolean tienePermiso(Usuario usuario, Permiso permiso) {

        if (usuario.getRol() == Rol.SUPERADMIN) {
            return true;
        }

        Set<Permiso> permisosEfectivos = EnumSet.copyOf(rolPermisosService.obtenerPermisosPorRol(usuario.getRol()));

        List<UsuarioPermiso> permisosAdicionales = usuarioPermisoRepository.findByUsuarioId(usuario.getId());

        for (UsuarioPermiso usuarioPermiso : permisosAdicionales) {
            permisosEfectivos.add(usuarioPermiso.getPermiso());
        }

        return permisosEfectivos.contains(permiso);
    }

}
