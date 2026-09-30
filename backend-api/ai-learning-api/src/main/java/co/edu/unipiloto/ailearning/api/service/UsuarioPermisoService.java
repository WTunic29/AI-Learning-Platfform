package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Permiso;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.model.UsuarioPermiso;
import co.edu.unipiloto.ailearning.api.repository.UsuarioPermisoRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.EnumSet;

@Service
public class UsuarioPermisoService {

    private final UsuarioPermisoRepository usuarioPermisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolPermisosService rolPermisosService;
    private final AutorizacionService autorizacionService;

    public UsuarioPermisoService(UsuarioPermisoRepository usuarioPermisoRepository,
            UsuarioRepository usuarioRepository, RolPermisosService rolPermisosService,
                                 AutorizacionService autorizacionService) {

        this.usuarioPermisoRepository = usuarioPermisoRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolPermisosService = rolPermisosService;
        this.autorizacionService = autorizacionService;

    }

    public UsuarioPermiso concederPermiso(Long usuarioId, Permiso permiso, Long actorId) {

        Usuario actor = usuarioRepository.findById(actorId).orElseThrow(() ->
                        new RuntimeException("El usuario que realiza la operación no existe"));

        if (!autorizacionService.tienePermiso(actor, Permiso.GESTIONAR_PERMISOS)) {
            throw new AccesoDenegadoException("El usuario no tiene permiso para gestionar permisos");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() ->
                        new RuntimeException("El usuario no existe"));

        if (usuarioPermisoRepository.existsByUsuarioIdAndPermiso(usuarioId, permiso)) {
            throw new RuntimeException("El usuario ya tiene este permiso");
        }

        UsuarioPermiso usuarioPermiso = new UsuarioPermiso();

        usuarioPermiso.setUsuario(usuario);
        usuarioPermiso.setPermiso(permiso);

        return usuarioPermisoRepository.save(usuarioPermiso);
    }

    public List<UsuarioPermiso> obtenerPermisos(Long usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("El usuario no existe");
        }

        return usuarioPermisoRepository.findByUsuarioId(usuarioId);
    }

    public Set<Permiso> obtenerPermisosEfectivos(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("El usuario no existe")
                );

        Set<Permiso> permisosEfectivos =
                EnumSet.copyOf(
                        rolPermisosService
                                .obtenerPermisosPorRol(usuario.getRol())
                );

        List<UsuarioPermiso> permisosAdicionales =
                usuarioPermisoRepository
                        .findByUsuarioId(usuarioId);

        for (UsuarioPermiso usuarioPermiso : permisosAdicionales) {
            permisosEfectivos.add(
                    usuarioPermiso.getPermiso()
            );
        }

        return permisosEfectivos;
    }

    @Transactional
    public void revocarPermiso(Long usuarioId, Permiso permiso, Long actorId) {

        Usuario actor = usuarioRepository.findById(actorId).orElseThrow(() ->
                        new RuntimeException("El usuario que realiza la operación no existe"));

        if (!autorizacionService.tienePermiso(actor, Permiso.GESTIONAR_PERMISOS)) {
            throw new AccesoDenegadoException("El usuario no tiene permiso para gestionar permisos");
        }

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("El usuario no existe");
        }

        if (!usuarioPermisoRepository.existsByUsuarioIdAndPermiso(usuarioId, permiso)) {
            throw new RuntimeException("El usuario no tiene este permiso");
        }

        usuarioPermisoRepository.deleteByUsuarioIdAndPermiso(usuarioId, permiso);
    }

}
