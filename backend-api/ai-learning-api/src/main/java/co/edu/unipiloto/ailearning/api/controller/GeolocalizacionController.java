package co.edu.unipiloto.ailearning.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@RestController
@RequestMapping("/api/geolocalizacion")
public class GeolocalizacionController {

    private static final String OVERPASS_PRINCIPAL =
            "https://maps.mail.ru/osm/tools/overpass/api/interpreter";
    private static final String OVERPASS_RESPALDO =
            "https://overpass.private.coffee/api/interpreter";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    @GetMapping(value = "/cercanos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cercanos(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5000") int radio,
            @RequestParam(defaultValue = "todos") String tipo) {

        int radioSeguro = Math.max(500, Math.min(radio, 10000));
        String consulta = construirConsulta(lat, lon, radioSeguro, tipo);

        ResponseEntity<String> respuesta = intentar(OVERPASS_PRINCIPAL, consulta);
        if (respuesta.getStatusCode().is2xxSuccessful()) return respuesta;

        respuesta = intentar(OVERPASS_RESPALDO, consulta);
        if (respuesta.getStatusCode().is2xxSuccessful()) return respuesta;

        return ResponseEntity.status(502)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"error\":\"Servicio geografico temporalmente no disponible\"}");
    }

    private ResponseEntity<String> intentar(String endpoint, String consulta) {
        try {
            String cuerpo = "data=" + URLEncoder.encode(consulta, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .timeout(Duration.ofSeconds(35))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("User-Agent", "NexaLearn-Academic/1.0")
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.body());
            }

            System.out.println("Overpass HTTP " + response.statusCode() + " en " + endpoint);
            return ResponseEntity.status(response.statusCode()).body("{}");
        } catch (Exception ex) {
            System.out.println("Fallo Overpass " + endpoint + ": " + ex.getMessage());
            return ResponseEntity.status(502).body("{}");
        }
    }

    private String construirConsulta(double lat, double lon, int radio, String tipo) {
        String around = "(around:" + radio + "," + lat + "," + lon + ")";
        String filtros;

        switch (tipo == null ? "todos" : tipo) {
            case "universidad":
                filtros = "nwr" + around + "[\"amenity\"=\"university\"];" +
                           "nwr" + around + "[\"amenity\"=\"college\"];";
                break;
            case "biblioteca":
                filtros = "nwr" + around + "[\"amenity\"=\"library\"];";
                break;
            case "colegio":
                filtros = "nwr" + around + "[\"amenity\"=\"school\"];";
                break;
            default:
                filtros = "nwr" + around + "[\"amenity\"=\"university\"];" +
                           "nwr" + around + "[\"amenity\"=\"college\"];" +
                           "nwr" + around + "[\"amenity\"=\"library\"];" +
                           "nwr" + around + "[\"amenity\"=\"school\"];";
        }

        return "[out:json][timeout:20];(" + filtros + ");out center tags;";
    }
}