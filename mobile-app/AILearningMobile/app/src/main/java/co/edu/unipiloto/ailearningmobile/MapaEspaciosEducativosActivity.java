package co.edu.unipiloto.ailearningmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.EspacioEducativoCercanoResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaEspaciosEducativosActivity extends AppCompatActivity {

    private TextView textEstadoUbicacion;
    private WebView webViewMapa;
    private Button btnVolverHome;

    private FusedLocationProviderClient proveedorUbicacion;
    private ActivityResultLauncher<String[]> solicitudPermisosUbicacion;

    private Double ultimaLatitud;
    private Double ultimaLongitud;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_espacios_educativos);

        textEstadoUbicacion = findViewById(R.id.textEstadoUbicacion);
        webViewMapa = findViewById(R.id.webViewMapa);
        btnVolverHome = findViewById(R.id.btnVolverHome);

        proveedorUbicacion =
                LocationServices.getFusedLocationProviderClient(this);

        configurarWebView();
        configurarPermisos();

        btnVolverHome.setOnClickListener(v -> finish());

        textEstadoUbicacion.setText("Preparando el mapa...");
        webViewMapa.loadUrl(
                "file:///android_asset/mapa_espacios_educativos.html"
        );
    }

    private void configurarWebView() {
        WebSettings settings = webViewMapa.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webViewMapa.addJavascriptInterface(
                new MapaJavaScriptInterface(),
                "AndroidMapa"
        );

        webViewMapa.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(
                    WebView view,
                    String url) {

                super.onPageFinished(view, url);

                textEstadoUbicacion.setText(
                        "Mapa listo. Obteniendo tu ubicacion..."
                );

                solicitarPermisosUbicacion();
            }
        });
    }

    private void configurarPermisos() {
        solicitudPermisosUbicacion =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        resultado -> {

                            boolean precisa =
                                    Boolean.TRUE.equals(
                                            resultado.get(
                                                    Manifest.permission.ACCESS_FINE_LOCATION
                                            )
                                    );

                            boolean aproximada =
                                    Boolean.TRUE.equals(
                                            resultado.get(
                                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                    );

                            if (precisa || aproximada) {
                                obtenerUbicacion();
                            } else {
                                textEstadoUbicacion.setText(
                                        "Permiso de ubicacion denegado."
                                );
                            }
                        }
                );
    }

    private void solicitarPermisosUbicacion() {
        boolean precisa =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        boolean aproximada =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        if (precisa || aproximada) {
            obtenerUbicacion();
            return;
        }

        solicitudPermisosUbicacion.launch(
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }
        );
    }

    private void obtenerUbicacion() {
        boolean precisa =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        boolean aproximada =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        if (!precisa && !aproximada) {
            return;
        }

        textEstadoUbicacion.setText(
                "Obteniendo ubicacion actual..."
        );

        CurrentLocationRequest request =
                new CurrentLocationRequest.Builder()
                        .setPriority(
                                precisa
                                        ? Priority.PRIORITY_HIGH_ACCURACY
                                        : Priority.PRIORITY_BALANCED_POWER_ACCURACY
                        )
                        .setDurationMillis(15000)
                        .build();

        proveedorUbicacion
                .getCurrentLocation(request, null)
                .addOnSuccessListener(this, ubicacion -> {
                    if (ubicacion == null) {
                        textEstadoUbicacion.setText(
                                "No se pudo obtener la ubicacion actual."
                        );
                        return;
                    }

                    ultimaLatitud = ubicacion.getLatitude();
                    ultimaLongitud = ubicacion.getLongitude();

                    webViewMapa.evaluateJavascript(
                            "mostrarUbicacionEstudiante(" +
                                    ultimaLatitud + "," +
                                    ultimaLongitud + ");",
                            null
                    );

                    textEstadoUbicacion.setText(
                            "Tu ubicacion se ha identificado correctamente."
                    );

                    consultarCercanos(
                            "todos",
                            5.0
                    );
                })
                .addOnFailureListener(this, error ->
                        textEstadoUbicacion.setText(
                                "Error al obtener la ubicacion."
                        )
                );
    }

    private void consultarCercanos(
            String tipo,
            double radioKm) {

        if (ultimaLatitud == null ||
                ultimaLongitud == null) {
            return;
        }

        webViewMapa.evaluateJavascript(
                "mostrarCarga();",
                null
        );

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService
                .obtenerEspaciosCercanosCache(
                        ultimaLatitud,
                        ultimaLongitud,
                        radioKm,
                        tipo
                )
                .enqueue(
                        new Callback<List<EspacioEducativoCercanoResponse>>() {

                            @Override
                            public void onResponse(
                                    Call<List<EspacioEducativoCercanoResponse>> call,
                                    Response<List<EspacioEducativoCercanoResponse>> response) {

                                if (!response.isSuccessful() ||
                                        response.body() == null) {

                                    webViewMapa.evaluateJavascript(
                                            "mostrarError('No fue posible consultar los espacios cercanos.');",
                                            null
                                    );
                                    return;
                                }

                                String json =
                                        gson.toJson(
                                                response.body()
                                        );

                                webViewMapa.evaluateJavascript(
                                        "recibirEspacios(" +
                                                json +
                                                ");",
                                        null
                                );
                            }

                            @Override
                            public void onFailure(
                                    Call<List<EspacioEducativoCercanoResponse>> call,
                                    Throwable t) {

                                webViewMapa.evaluateJavascript(
                                        "mostrarError('No fue posible conectar con NexaLearn.');",
                                        null
                                );
                            }
                        }
                );
    }

    private void abrirNavegacion(
            double latitud,
            double longitud,
            String nombre) {

        String destino =
                latitud + "," + longitud;

        Uri uri =
                Uri.parse(
                        "geo:0,0?q=" +
                                Uri.encode(
                                        destino +
                                                "(" +
                                                nombre +
                                                ")"
                                )
                );

        Intent intent =
                new Intent(
                        Intent.ACTION_VIEW,
                        uri
                );

        try {
            startActivity(intent);
        } catch (Exception ex) {
            Uri web =
                    Uri.parse(
                            "https://www.openstreetmap.org/?mlat=" +
                                    latitud +
                                    "&mlon=" +
                                    longitud +
                                    "#map=16/" +
                                    latitud +
                                    "/" +
                                    longitud
                    );

            startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            web
                    )
            );
        }
    }

    public class MapaJavaScriptInterface {

        @JavascriptInterface
        public void consultar(
                String tipo,
                double radioKm) {

            runOnUiThread(() ->
                    consultarCercanos(
                            tipo,
                            radioKm
                    )
            );
        }

        @JavascriptInterface
        public void navegar(
                double latitud,
                double longitud,
                String nombre) {

            runOnUiThread(() ->
                    abrirNavegacion(
                            latitud,
                            longitud,
                            nombre
                    )
            );
        }
    }
}