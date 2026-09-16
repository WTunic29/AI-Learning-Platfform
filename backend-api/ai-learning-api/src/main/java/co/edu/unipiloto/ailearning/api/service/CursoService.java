package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.CursoResponse;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import org.springframework.stereotype.Service;

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
