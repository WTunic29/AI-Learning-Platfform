package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.model.Inscripcion;

import co.edu.unipiloto.ailearning.api.dto.CursoProgresoResumenResponse;
import co.edu.unipiloto.ailearning.api.dto.ModuloProgresoResponse;
import co.edu.unipiloto.ailearning.api.dto.ProgresoCursoResponse;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.Modulo;
import co.edu.unipiloto.ailearning.api.model.ProgresoModulo;
import co.edu.unipiloto.ailearning.api.model.Rol;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.InscripcionRepository;
import co.edu.unipiloto.ailearning.api.repository.ModuloRepository;
import co.edu.unipiloto.ailearning.api.repository.ProgresoModuloRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProgresoService {

    private final ProgresoModuloRepository progresoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CursoRepository cursoRepository;

    public ProgresoService(ProgresoModuloRepository progresoRepository, UsuarioRepository usuarioRepository,
                           ModuloRepository moduloRepository, InscripcionRepository inscripcionRepository, CursoRepository cursoRepository) {

        this.progresoRepository = progresoRepository;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.cursoRepository = cursoRepository;

    }

    @Transactional
    public ProgresoModulo marcarModuloCompletado(Long usuarioId, Long moduloId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() ->
                        new IllegalArgumentException("El usuario no existe"));

        if (usuario.getRol() != Rol.ESTUDIANTE) {
            throw new AccesoDenegadoException("Solo los estudiantes pueden registrar su progreso");
        }

        Modulo modulo = moduloRepository.findById(moduloId).orElseThrow(() ->
                        new IllegalArgumentException("El mÃƒÂ³dulo no existe"));

        Long cursoId = modulo.getCurso().getId();

        boolean inscrito = inscripcionRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId);

        if (!inscrito) {
            throw new AccesoDenegadoException("El estudiante no estÃƒÂ¡ inscrito en este curso");
        }

        return progresoRepository.findByUsuarioIdAndModuloId(usuarioId, moduloId).orElseGet(() -> {

                    ProgresoModulo progreso = new ProgresoModulo();

                    progreso.setUsuario(usuario);
                    progreso.setModulo(modulo);

                    return progresoRepository.save(progreso);

                });

    }

    @Transactional(readOnly = true)
    public ProgresoCursoResponse obtenerProgresoCurso(Long usuarioId, Long cursoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() ->
                        new IllegalArgumentException("El usuario no existe"));

        boolean inscrito = inscripcionRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId);

        if (usuario.getRol() != Rol.ESTUDIANTE || !inscrito) {
            throw new AccesoDenegadoException("No tienes acceso al progreso de este curso");
        }

        var curso = cursoRepository.findById(cursoId).orElseThrow(() ->
                        new IllegalArgumentException("El curso no existe"));

        List<Modulo> modulos = moduloRepository.findByCursoIdOrderByOrdenAsc(cursoId);
        List<ProgresoModulo> progresos = progresoRepository.findByUsuarioIdAndModuloCursoId(usuarioId, cursoId);

        Map<Long, ProgresoModulo> progresosPorModulo = progresos.stream().collect(Collectors.toMap(
                        progreso -> progreso.getModulo().getId(), Function.identity()));
        Set<Long> modulosCompletadosIds = progresosPorModulo.keySet();

        List<ModuloProgresoResponse> detalleModulos = modulos.stream().map(modulo -> {

                            ProgresoModulo progreso = progresosPorModulo.get(modulo.getId());
                            boolean completado = progreso != null;

                            return new ModuloProgresoResponse(modulo.getId(),
                                    modulo.getTitulo(), completado, completado ? progreso.getFechaCompletado() : null);

                        }).toList();

        int totalModulos = modulos.size();
        int completados = modulosCompletadosIds.size();
        int pendientes = totalModulos - completados;

        double porcentaje = totalModulos == 0 ? 0.0 : Math.round((completados * 100.0 / totalModulos) * 100.0) / 100.0;

        return new ProgresoCursoResponse(curso.getId(), curso.getNombre(), totalModulos, completados, pendientes, porcentaje, detalleModulos);
    }

    public List<CursoProgresoResumenResponse> obtenerMisCursos(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario no existe"
                        )
                );

        if (usuario.getRol() != Rol.ESTUDIANTE) {
            throw new AccesoDenegadoException(
                    "Solo los estudiantes pueden consultar sus cursos"
            );
        }

        List<Inscripcion> inscripciones =
                inscripcionRepository.findByUsuarioId(usuarioId);

        return inscripciones.stream()
                .map(inscripcion -> {

                    Curso curso = inscripcion.getCurso();

                    ProgresoCursoResponse progreso =
                            obtenerProgresoCurso(
                                    usuarioId,
                                    curso.getId()
                            );

                    double porcentaje =
                            progreso.getPorcentajeAvance();

                    String estado =
                            porcentaje >= 100.0
                                    ? "FINALIZADO"
                                    : "EN_PROGRESO";

                    return new CursoProgresoResumenResponse(
                            curso.getId(),
                            curso.getNombre(),
                            porcentaje,
                            estado
                    );
                })
                .toList();
    }

}