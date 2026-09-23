package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.ModuloResponse;
import co.edu.unipiloto.ailearning.api.dto.RecursoResponse;
import co.edu.unipiloto.ailearning.api.model.Modulo;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.InscripcionRepository;
import co.edu.unipiloto.ailearning.api.repository.ModuloRepository;
import co.edu.unipiloto.ailearning.api.repository.RecursoRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContenidoService {

    private final ModuloRepository moduloRepository;
    private final RecursoRepository recursoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public ContenidoService(
            ModuloRepository moduloRepository,
            RecursoRepository recursoRepository,
            InscripcionRepository inscripcionRepository,
            UsuarioRepository usuarioRepository,
            CursoRepository cursoRepository) {

        this.moduloRepository = moduloRepository;
        this.recursoRepository = recursoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<ModuloResponse> obtenerContenido(
            Long usuarioId,
            Long cursoId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El usuario no existe"
            );
        }

        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El curso no existe"
            );
        }

        if (!inscripcionRepository
                .existsByUsuarioIdAndCursoId(usuarioId, cursoId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El usuario no está inscrito en este curso"
            );
        }

        return moduloRepository
                .findByCursoIdOrderByOrdenAsc(cursoId)
                .stream()
                .map(this::convertirModulo)
                .toList();
    }

    private ModuloResponse convertirModulo(Modulo modulo) {

        List<RecursoResponse> recursos = recursoRepository
                .findByModuloIdOrderByOrdenAsc(modulo.getId())
                .stream()
                .map(recurso -> new RecursoResponse(
                        recurso.getId(),
                        recurso.getTitulo(),
                        recurso.getTipo(),
                        recurso.getUrl(),
                        recurso.getOrden()
                ))
                .toList();

        return new ModuloResponse(
                modulo.getId(),
                modulo.getTitulo(),
                modulo.getDescripcion(),
                modulo.getOrden(),
                recursos
        );
    }
}