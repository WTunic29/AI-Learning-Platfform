package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.CursoRequest;
import co.edu.unipiloto.ailearning.api.dto.CursoResponse;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.model.Rol;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public CursoService(
            CursoRepository cursoRepository,
            UsuarioRepository usuarioRepository,
            NotificacionService notificacionService) {

        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    public List<CursoResponse> obtenerTodosLosCursos() {
        return cursoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public CursoResponse obtenerCursoPorId(Long id) {
        Curso curso = obtenerEntidad(id);
        return convertirAResponse(curso);
    }

    public CursoResponse crearCurso(CursoRequest request) {

        validarFechas(request);

        Curso curso = new Curso();

        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setCategoria(request.getCategoria());

        aplicarDatosAcademicos(curso, request);

        if (request.getDocenteId() != null) {
            Usuario docente = obtenerDocente(request.getDocenteId());
            curso.setDocente(docente);
        }

        Curso guardado = cursoRepository.save(curso);

        if (guardado.getDocente() != null) {
            notificarAsignacion(guardado, guardado.getDocente());
        }

        return convertirAResponse(guardado);
    }

    public CursoResponse actualizarCurso(Long id, CursoRequest request) {

        validarFechas(request);

        Curso curso = obtenerEntidad(id);

        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setCategoria(request.getCategoria());

        aplicarDatosAcademicos(curso, request);

        Long docenteAnterior = curso.getDocente() == null
                ? null
                : curso.getDocente().getId();

        if (request.getDocenteId() != null) {
            Usuario docente = obtenerDocente(request.getDocenteId());
            curso.setDocente(docente);
        }

        Curso actualizado = cursoRepository.save(curso);

        Long docenteNuevo = actualizado.getDocente() == null
                ? null
                : actualizado.getDocente().getId();

        if (docenteNuevo != null &&
                !Objects.equals(docenteAnterior, docenteNuevo)) {

            notificarAsignacion(
                    actualizado,
                    actualizado.getDocente()
            );
        }

        return convertirAResponse(actualizado);
    }

    public CursoResponse asignarDocente(
            Long cursoId,
            Long docenteId) {

        Curso curso = obtenerEntidad(cursoId);
        Usuario docente = obtenerDocente(docenteId);

        curso.setDocente(docente);

        Curso actualizado = cursoRepository.save(curso);

        notificarAsignacion(actualizado, docente);

        return convertirAResponse(actualizado);
    }

    public void eliminarCurso(Long id) {
        Curso curso = obtenerEntidad(id);
        cursoRepository.delete(curso);
    }

    public List<CursoResponse> buscarCursos(String termino) {

        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodosLosCursos();
        }

        String busqueda = termino.trim();

        return cursoRepository
                .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
                        busqueda,
                        busqueda,
                        busqueda
                )
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private Curso obtenerEntidad(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Curso no encontrado"));
    }

    private Usuario obtenerDocente(Long docenteId) {

        Usuario usuario = usuarioRepository.findById(docenteId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Docente no encontrado"));

        if (usuario.getRol() != Rol.DOCENTE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario seleccionado no tiene rol DOCENTE"
            );
        }

        return usuario;
    }

    private void aplicarDatosAcademicos(
            Curso curso,
            CursoRequest request) {

        if (request.getObjetivo() != null) {
            curso.setObjetivo(request.getObjetivo());
        }

        if (request.getResultadosEsperados() != null) {
            curso.setResultadosEsperados(
                    request.getResultadosEsperados()
            );
        }

        if (request.getCupoMaximo() != null) {

            if (request.getCupoMaximo() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El cupo maximo debe ser mayor a cero"
                );
            }

            curso.setCupoMaximo(
                    request.getCupoMaximo()
            );
        }

        if (request.getFechaInicio() != null) {
            curso.setFechaInicio(request.getFechaInicio());
        }

        if (request.getFechaFin() != null) {
            curso.setFechaFin(request.getFechaFin());
        }
    }

    private void validarFechas(CursoRequest request) {

        if (request.getFechaInicio() != null &&
                request.getFechaFin() != null &&
                request.getFechaFin()
                        .isBefore(request.getFechaInicio())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha fin no puede ser anterior a la fecha inicio"
            );
        }
    }

    private void notificarAsignacion(
            Curso curso,
            Usuario docente) {

        notificacionService.crear(
                docente,
                "ASIGNACION_CURSO",
                "Has sido asignado al curso: " +
                        curso.getNombre()
        );
    }

    private CursoResponse convertirAResponse(
            Curso curso) {

        Long docenteId = null;
        String docenteNombre = null;

        if (curso.getDocente() != null) {
            docenteId = curso.getDocente().getId();
            docenteNombre = curso.getDocente().getNombre();
        }

        return new CursoResponse(
                curso.getId(),
                curso.getNombre(),
                curso.getDescripcion(),
                curso.getCategoria(),
                curso.getObjetivo(),
                curso.getResultadosEsperados(),
                curso.getCupoMaximo(),
                curso.getFechaInicio(),
                curso.getFechaFin(),
                docenteId,
                docenteNombre
        );
    }
}