package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.RecursoCrudRequest;
import co.edu.unipiloto.ailearning.api.dto.RecursoCrudResponse;
import co.edu.unipiloto.ailearning.api.model.Modulo;
import co.edu.unipiloto.ailearning.api.model.Recurso;
import co.edu.unipiloto.ailearning.api.repository.ModuloRepository;
import co.edu.unipiloto.ailearning.api.repository.RecursoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final ModuloRepository moduloRepository;

    public RecursoService(
            RecursoRepository recursoRepository,
            ModuloRepository moduloRepository) {

        this.recursoRepository = recursoRepository;
        this.moduloRepository = moduloRepository;
    }

    public List<RecursoCrudResponse> obtenerTodos() {

        return recursoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public RecursoCrudResponse obtenerPorId(Long id) {

        return convertirAResponse(
                buscarRecurso(id)
        );
    }

    public List<RecursoCrudResponse> obtenerPorModulo(Long moduloId) {

        if (!moduloRepository.existsById(moduloId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Módulo no encontrado"
            );
        }

        return recursoRepository
                .findByModuloIdOrderByOrdenAsc(moduloId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public RecursoCrudResponse crear(
            RecursoCrudRequest request) {

        validarRequest(request);

        Modulo modulo = buscarModulo(request.getModuloId());

        Recurso recurso = new Recurso();

        recurso.setModulo(modulo);
        recurso.setTitulo(request.getTitulo().trim());
        recurso.setTipo(request.getTipo().trim());
        recurso.setUrl(request.getUrl().trim());
        recurso.setOrden(request.getOrden());

        return convertirAResponse(
                recursoRepository.save(recurso)
        );
    }

    public RecursoCrudResponse actualizar(
            Long id,
            RecursoCrudRequest request) {

        validarRequest(request);

        Recurso recurso = buscarRecurso(id);
        Modulo modulo = buscarModulo(request.getModuloId());

        recurso.setModulo(modulo);
        recurso.setTitulo(request.getTitulo().trim());
        recurso.setTipo(request.getTipo().trim());
        recurso.setUrl(request.getUrl().trim());
        recurso.setOrden(request.getOrden());

        return convertirAResponse(
                recursoRepository.save(recurso)
        );
    }

    public void eliminar(Long id) {

        Recurso recurso = buscarRecurso(id);

        recursoRepository.delete(recurso);
    }

    private Modulo buscarModulo(Long id) {

        return moduloRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Módulo no encontrado"
                        )
                );
    }

    private Recurso buscarRecurso(Long id) {

        return recursoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Recurso no encontrado"
                        )
                );
    }

    private void validarRequest(
            RecursoCrudRequest request) {

        if (request == null
                || request.getModuloId() == null
                || request.getTitulo() == null
                || request.getTitulo().trim().isEmpty()
                || request.getTipo() == null
                || request.getTipo().trim().isEmpty()
                || request.getUrl() == null
                || request.getUrl().trim().isEmpty()
                || request.getOrden() == null
                || request.getOrden() <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "moduloId, titulo, tipo, url y orden válido son obligatorios"
            );
        }
    }

    private RecursoCrudResponse convertirAResponse(
            Recurso recurso) {

        return new RecursoCrudResponse(
                recurso.getId(),
                recurso.getModulo().getId(),
                recurso.getModulo().getTitulo(),
                recurso.getTitulo(),
                recurso.getTipo(),
                recurso.getUrl(),
                recurso.getOrden()
        );
    }
}