package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.ActividadResponse;
import co.edu.unipiloto.ailearning.api.model.Actividad;
import co.edu.unipiloto.ailearning.api.model.Curso;
import co.edu.unipiloto.ailearning.api.repository.ActivityRepository;
import co.edu.unipiloto.ailearning.api.repository.CursoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    private final ActivityRepository activityRepository;
    private final CursoRepository cursoRepository;

    public ActividadService(ActivityRepository activityRepository, CursoRepository cursoRepository) {

        this.activityRepository = activityRepository;
        this.cursoRepository = cursoRepository;

    }

    public ActividadResponse crearActividad(Long cursoId, Actividad actividad){

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("El curso no existe"));

        actividad.setCurso(curso);
        Actividad guardada = activityRepository.save(actividad);

        return convertirAResponse(guardada);

    }

    public List<ActividadResponse> obtenerActividades(Long cursoId){

        if (!cursoRepository.existsById(cursoId)){
            throw new RuntimeException("El curso no existe");
        }

        return  activityRepository.findByCursoId(cursoId).
                stream().map(this::convertirAResponse).toList();

    }

    private ActividadResponse convertirAResponse(Actividad actividad){

        return new ActividadResponse(actividad.getId(),
                actividad.getCurso().getId(),
                actividad.getDescripcion(),
                actividad.getFecha(),
                actividad.getPonderacion()

        );

    }

}
