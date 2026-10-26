package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.CursoRequest;
import co.edu.unipiloto.ailearning.api.dto.CursoResponse;
import co.edu.unipiloto.ailearning.api.service.CursoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService){
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<List<CursoResponse>> obtenerCursos(){

        return ResponseEntity.ok(
                cursoService.obtenerTodosLosCursos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> obtenerCursoPorId(
            @PathVariable Long id){

        return ResponseEntity.ok(
                cursoService.obtenerCursoPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<CursoResponse> crearCurso(
            @RequestBody CursoRequest request){

        return ResponseEntity.ok(
                cursoService.crearCurso(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponse> actualizarCurso(
            @PathVariable Long id,
            @RequestBody CursoRequest request){

        return ResponseEntity.ok(
                cursoService.actualizarCurso(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(
            @PathVariable Long id){

        cursoService.eliminarCurso(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CursoResponse>> buscarCursos(
            @RequestParam String termino){

        return ResponseEntity.ok(
                cursoService.buscarCursos(termino)
        );
    }
}