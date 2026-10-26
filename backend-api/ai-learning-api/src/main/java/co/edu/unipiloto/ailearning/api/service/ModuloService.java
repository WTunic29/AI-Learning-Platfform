package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.ModuloCrudRequest;
import co.edu.unipiloto.ailearning.api.dto.ModuloCrudResponse;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.model.Modulo;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.ModuloRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final CursoRepository cursoRepository;

    public ModuloService(
            ModuloRepository moduloRepository,
            CursoRepository cursoRepository) {

        this.moduloRepository = moduloRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<ModuloCrudResponse> obtenerTodos() {

        return moduloRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public ModuloCrudResponse obtenerPorId(Long id) {

        Modulo modulo = buscarModulo(id);

        return convertirAResponse(modulo);
    }

    public List<ModuloCrudResponse> obtenerPorCurso(Long cursoId) {

        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Curso no encontrado"
            );
        }

        return moduloRepository
                .findByCursoIdOrderByOrdenAsc(cursoId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public ModuloCrudResponse crear(ModuloCrudRequest request) {

        validarRequest(request);

        Curso curso = buscarCurso(request.getCursoId());

        Modulo modulo = new Modulo();

        modulo.setCurso(curso);
        modulo.setTitulo(request.getTitulo().trim());
        modulo.setDescripcion(request.getDescripcion().trim());
        modulo.setOrden(request.getOrden());

        return convertirAResponse(
                moduloRepository.save(modulo)
        );
    }

    public ModuloCrudResponse actualizar(
            Long id,
            ModuloCrudRequest request) {

        validarRequest(request);

        Modulo modulo = buscarModulo(id);
        Curso curso = buscarCurso(request.getCursoId());

        modulo.setCurso(curso);
        modulo.setTitulo(request.getTitulo().trim());
        modulo.setDescripcion(request.getDescripcion().trim());
        modulo.setOrden(request.getOrden());

        return convertirAResponse(
                moduloRepository.save(modulo)
        );
    }

    public void eliminar(Long id) {

        Modulo modulo = buscarModulo(id);

        try {

            moduloRepository.delete(modulo);
            moduloRepository.flush();

        } catch (DataIntegrityViolationException e) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el mÃ³dulo porque tiene informaciÃ³n asociada"
            );
        }
    }

    private Curso buscarCurso(Long id) {

        return cursoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Curso no encontrado"
                        )
                );
    }

    private Modulo buscarModulo(Long id) {

        return moduloRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "MÃ³dulo no encontrado"
                        )
                );
    }

    private void validarRequest(ModuloCrudRequest request) {

        if (request == null
                || request.getCursoId() == null
                || request.getTitulo() == null
                || request.getTitulo().trim().isEmpty()
                || request.getDescripcion() == null
                || request.getDescripcion().trim().isEmpty()
                || request.getOrden() == null
                || request.getOrden() <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "cursoId, titulo, descripcion y orden vÃ¡lido son obligatorios"
            );
        }
    }

    private ModuloCrudResponse convertirAResponse(Modulo modulo) {

        return new ModuloCrudResponse(
                modulo.getId(),
                modulo.getCurso().getId(),
                modulo.getCurso().getNombre(),
                modulo.getTitulo(),
                modulo.getDescripcion(),
                modulo.getOrden()
        );
    }
}