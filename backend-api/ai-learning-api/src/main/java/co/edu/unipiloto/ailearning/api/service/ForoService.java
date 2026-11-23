package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.*;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ForoService {

    private final MensajeForoRepository mensajeRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public ForoService(
            MensajeForoRepository mensajeRepository,
            CursoRepository cursoRepository,
            UsuarioRepository usuarioRepository) {

        this.mensajeRepository = mensajeRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public MensajeForoResponse publicar(
            Long cursoId,
            MensajeForoRequest request) {

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Curso no encontrado"));

        Usuario usuario = usuarioRepository
                .findById(request.usuarioId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        MensajeForo mensaje = new MensajeForo();
        mensaje.setCurso(curso);
        mensaje.setUsuario(usuario);
        mensaje.setMensaje(request.mensaje());

        return toResponse(
                mensajeRepository.save(mensaje)
        );
    }

    public List<MensajeForoResponse> listar(Long cursoId) {

        return mensajeRepository
                .findByCursoIdOrderByFechaCreacionAsc(
                        cursoId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MensajeForoResponse toResponse(
            MensajeForo mensaje) {

        return new MensajeForoResponse(
                mensaje.getId(),
                mensaje.getCurso().getId(),
                mensaje.getUsuario().getId(),
                mensaje.getUsuario().getNombre(),
                mensaje.getMensaje(),
                mensaje.getFechaCreacion()
        );
    }
}