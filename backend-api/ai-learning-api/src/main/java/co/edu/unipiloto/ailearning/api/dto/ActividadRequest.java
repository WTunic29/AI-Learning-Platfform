package co.edu.unipiloto.ailearning.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ActividadRequest(
        String descripcion,
        LocalDate fecha,
        BigDecimal ponderacion
) {
}