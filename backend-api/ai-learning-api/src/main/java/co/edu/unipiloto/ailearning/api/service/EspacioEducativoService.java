package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.EspacioEducativoCercanoResponse;
import co.edu.unipiloto.ailearning.api.model.EspacioEducativo;
import co.edu.unipiloto.ailearning.api.repository.EspacioEducativoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class EspacioEducativoService {

    private final EspacioEducativoRepository espacioEducativoRepository;

    public EspacioEducativoService(
            EspacioEducativoRepository espacioEducativoRepository) {
        this.espacioEducativoRepository = espacioEducativoRepository;
    }

    public List<EspacioEducativo> obtenerEspaciosActivos() {
        return espacioEducativoRepository.findByActivoTrue();
    }

    public List<EspacioEducativoCercanoResponse> obtenerCercanos(
            double latitudUsuario,
            double longitudUsuario,
            double radioKm,
            String tipoSolicitado) {

        double radioSeguro =
                Math.max(0.5, Math.min(radioKm, 25.0));

        String filtro =
                tipoSolicitado == null
                        ? "todos"
                        : tipoSolicitado
                                .trim()
                                .toLowerCase(Locale.ROOT);

        List<EspacioEducativo> espacios =
                espacioEducativoRepository.findByActivoTrue();

        List<EspacioEducativoCercanoResponse> resultado =
                new ArrayList<>();

        int total = 0;
        int coincidenTipo = 0;
        int coordenadasValidas = 0;

        double distanciaMinima = Double.MAX_VALUE;
        String nombreMasCercano = "";

        for (EspacioEducativo espacio : espacios) {

            total++;

            String tipoDb =
                    espacio.getTipo() == null
                            ? ""
                            : espacio.getTipo()
                                    .toString()
                                    .trim()
                                    .toLowerCase(Locale.ROOT);

            if (!coincideTipo(tipoDb, filtro)) {
                continue;
            }

            coincidenTipo++;

            if (espacio.getLatitud() == null ||
                    espacio.getLongitud() == null) {
                continue;
            }

            double latitud;
            double longitud;

            try {
                latitud =
                        Double.parseDouble(
                                espacio.getLatitud().toString()
                        );

                longitud =
                        Double.parseDouble(
                                espacio.getLongitud().toString()
                        );

            } catch (Exception ex) {
                continue;
            }

            coordenadasValidas++;

            double distancia =
                    calcularDistanciaKm(
                            latitudUsuario,
                            longitudUsuario,
                            latitud,
                            longitud
                    );

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                nombreMasCercano = espacio.getNombre();
            }

            if (distancia > radioSeguro) {
                continue;
            }

            resultado.add(
                    new EspacioEducativoCercanoResponse(
                            espacio.getId(),
                            espacio.getNombre(),
                            espacio.getTipo().toString(),
                            espacio.getDireccion(),
                            espacio.getDescripcion(),
                            latitud,
                            longitud,
                            distancia
                    )
            );
        }

        resultado.sort(
                Comparator.comparingDouble(
                        EspacioEducativoCercanoResponse::getDistanciaKm
                )
        );

        System.out.println(
                "[GEO] filtro=" + filtro +
                " | radioKm=" + radioSeguro +
                " | total=" + total +
                " | coincidenTipo=" + coincidenTipo +
                " | coordenadasValidas=" + coordenadasValidas +
                " | dentroRadio=" + resultado.size() +
                " | masCercano=" + nombreMasCercano +
                " | distanciaMin=" +
                (distanciaMinima == Double.MAX_VALUE
                        ? "N/A"
                        : distanciaMinima)
        );

        return resultado;
    }

    private boolean coincideTipo(
            String tipoDb,
            String filtro) {

        if ("todos".equals(filtro) ||
                filtro.isBlank()) {
            return true;
        }

        if ("universidad".equals(filtro)) {
            return tipoDb.contains("universidad");
        }

        if ("biblioteca".equals(filtro)) {
            return tipoDb.contains("biblioteca");
        }

        if ("colegio".equals(filtro)) {
            return tipoDb.contains("colegio");
        }

        return true;
    }

    private double calcularDistanciaKm(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double RADIO_TIERRA_KM = 6371.0088;

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double dLat =
                Math.toRadians(lat2 - lat1);

        double dLon =
                Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2.0) *
                Math.sin(dLat / 2.0) +
                Math.cos(lat1Rad) *
                Math.cos(lat2Rad) *
                Math.sin(dLon / 2.0) *
                Math.sin(dLon / 2.0);

        double c =
                2.0 *
                Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1.0 - a)
                );

        return RADIO_TIERRA_KM * c;
    }
}