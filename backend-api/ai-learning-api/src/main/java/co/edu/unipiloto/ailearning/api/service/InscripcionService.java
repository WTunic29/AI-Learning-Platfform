package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.InscripcionResponse;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.model.Inscripcion;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;
import co.edu.unipiloto.ailearning.api.repository.InscripcionRepository;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import  org.springframework.stereotype.Service;
import  java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public InscripcionService
            (InscripcionRepository inscripcionRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository){

        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;

    }

    public InscripcionResponse inscribir(Long usuarioId, Long cursoId){

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("El curso no existe"));

        if (inscripcionRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId)){

            throw new RuntimeException("El estudiante ya está inscrito en el curso");

        }

        Inscripcion inscripcion = new Inscripcion();

        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(curso);

        Inscripcion guardada = inscripcionRepository.save(inscripcion);

        return  convertirAResponse(guardada);

    }

    public  List<InscripcionResponse> obtenerInscripciones(Long usuarioId){

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("El usuario no existe");
        }

        return inscripcionRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirAResponse).toList();

    }

    private InscripcionResponse convertirAResponse(Inscripcion inscripcion) {

        return new InscripcionResponse(inscripcion.getId(), inscripcion.getUsuario().getId(),
                inscripcion.getCurso().getId(), inscripcion.getCurso().getNombre(),
                inscripcion.getFechaInscripcion());

    }

}
