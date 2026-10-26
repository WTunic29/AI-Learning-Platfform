package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.UsuarioResponse;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AutorizacionService autorizacionService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            AutorizacionService autorizacionService) {

        this.usuarioRepository = usuarioRepository;
        this.autorizacionService = autorizacionService;
    }

    public Usuario cambiarRol(
            Long id,
            String nuevoRol,
            Long actorId) {

        Usuario actor = usuarioRepository.findById(actorId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El usuario que realiza la operación no existe"));

        if (!autorizacionService.tienePermiso(
                actor,
                Permiso.GESTIONAR_ROLES)) {

            throw new AccesoDenegadoException(
                    "El usuario no tiene permiso para gestionar roles");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        try {

            Rol rol = Rol.valueOf(
                    nuevoRol.toUpperCase().trim());

            usuario.setRol(rol);

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rol no válido");
        }

        return usuarioRepository.save(usuario);
    }

    public UsuarioResponse obtenerPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        return new UsuarioResponse(usuario);
    }

    public List<UsuarioResponse> buscarUsuarios(
            String termino) {

        if (termino == null
                || termino.trim().isEmpty()) {

            return List.of();
        }

        String busqueda = termino.trim();

        return usuarioRepository
                .findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                        busqueda,
                        busqueda)
                .stream()
                .map(UsuarioResponse::new)
                .toList();
    }

    public void eliminar(
            Long id,
            Long actorId) {

        if (id.equals(actorId)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Un usuario no puede eliminar su propia cuenta administrativa");
        }

        Usuario actor = usuarioRepository.findById(actorId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El usuario administrador no existe"));

        if (!autorizacionService.tienePermiso(
                actor,
                Permiso.GESTIONAR_ROLES)) {

            throw new AccesoDenegadoException(
                    "El usuario no tiene permiso para eliminar usuarios");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        try {

            usuarioRepository.delete(usuario);
            usuarioRepository.flush();

        } catch (DataIntegrityViolationException e) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el usuario porque tiene información académica asociada");
        }
    }
}