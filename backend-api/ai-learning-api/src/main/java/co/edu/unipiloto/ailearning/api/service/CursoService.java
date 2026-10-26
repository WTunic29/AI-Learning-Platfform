package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.CursoRequest;
import co.edu.unipiloto.ailearning.api.dto.CursoResponse;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository){
        this.cursoRepository = cursoRepository;
    }

    public List<CursoResponse> obtenerTodosLosCursos(){

        return cursoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public CursoResponse obtenerCursoPorId(Long id){

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        return convertirAResponse(curso);
    }

    public CursoResponse crearCurso(CursoRequest request){

        Curso curso = new Curso();

        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setCategoria(request.getCategoria());

        Curso guardado = cursoRepository.save(curso);

        return convertirAResponse(guardado);
    }

    public CursoResponse actualizarCurso(Long id, CursoRequest request){

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setCategoria(request.getCategoria());

        Curso actualizado = cursoRepository.save(curso);

        return convertirAResponse(actualizado);
    }

    public void eliminarCurso(Long id){

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        cursoRepository.delete(curso);
    }

    public List<CursoResponse> buscarCursos(String termino){

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

    private CursoResponse convertirAResponse(Curso curso){

        return new CursoResponse(
                curso.getId(),
                curso.getNombre(),
                curso.getDescripcion(),
                curso.getCategoria()
        );
    }
}