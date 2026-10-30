package co.edu.unipiloto.ailearning.api.repository;

import co.edu.unipiloto.ailearning.api.model.EspacioEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspacioEducativoRepository extends JpaRepository<EspacioEducativo, Long>{
    List<EspacioEducativo> findByActivoTrue();
}
