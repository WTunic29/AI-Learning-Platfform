package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.EspacioEducativoCercanoResponse;
import co.edu.unipiloto.ailearning.api.model.EspacioEducativo;
import co.edu.unipiloto.ailearning.api.service.EspacioEducativoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/espacios-educativos")
public class EspacioEducativoController {

    private final EspacioEducativoService espacioEducativoService;

    public EspacioEducativoController(
            EspacioEducativoService espacioEducativoService) {
        this.espacioEducativoService = espacioEducativoService;
    }

    @GetMapping
    public List<EspacioEducativo> obtenerEspaciosEducativos() {
        return espacioEducativoService.obtenerEspaciosActivos();
    }

    @GetMapping("/cercanos")
    public List<EspacioEducativoCercanoResponse> obtenerCercanos(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5") double radioKm,
            @RequestParam(defaultValue = "todos") String tipo) {

        return espacioEducativoService.obtenerCercanos(
                lat,
                lon,
                radioKm,
                tipo
        );
    }
}