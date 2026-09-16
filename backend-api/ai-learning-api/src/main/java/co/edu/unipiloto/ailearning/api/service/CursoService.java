package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.CursoResponse;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository cursoRepository; //Guarda una referencia al repositorio para poder consultar la DB


    public CursoService(CursoRepository cursoRepository){
        this.cursoRepository = cursoRepository;
    }

    // Este metodo devuelve una lista de respuestas de cursos
    public List<CursoResponse> obtenerTodosLosCursos(){

        return cursoRepository.findAll()// consulta todos los registros de la tabla cursos
                .stream()
                .map(this::convertirAResponse) // convierte curso - cursoAResponse
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
