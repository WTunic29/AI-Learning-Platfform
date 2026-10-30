package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.EspacioEducativo;
import co.edu.unipiloto.ailearning.api.repository.EspacioEducativoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspacioEducativoService {

    private final EspacioEducativoRepository espacioEducativoRepository;

    public EspacioEducativoService(EspacioEducativoRepository espacioEducativoRepository) {
        this.espacioEducativoRepository = espacioEducativoRepository;
    }

    public List<EspacioEducativo> obtenerEspaciosActivos() {
        return espacioEducativoRepository.findByActivoTrue();
    }

}