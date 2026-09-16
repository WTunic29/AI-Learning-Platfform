package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CursoRepository extends  JpaRepository<Curso, Long> {

    //uso automatico de operaciones Spring data jpa

}
