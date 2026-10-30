package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.model.EspacioEducativo;
import co.edu.unipiloto.ailearning.api.service.EspacioEducativoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/espacios-educativos")
public class EspacioEducativoController {

    private final EspacioEducativoService espacioEducativoService;

    public EspacioEducativoController(EspacioEducativoService espacioEducativoService) {
        this.espacioEducativoService = espacioEducativoService;
    }

    @GetMapping
    public List<EspacioEducativo> obtenerEspaciosEducativos() {
        return espacioEducativoService.obtenerEspaciosActivos();
    }

}
