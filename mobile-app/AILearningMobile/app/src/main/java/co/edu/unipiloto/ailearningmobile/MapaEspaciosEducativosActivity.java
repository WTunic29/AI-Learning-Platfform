package co.edu.unipiloto.ailearningmobile;

import android.util.Log;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.EspacioEducativoResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;


import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapaEspaciosEducativosActivity extends AppCompatActivity {

    private TextView textEstadoUbicacion;
    private WebView webViewMapa;
    private Button btnVolverHome;
    private FusedLocationProviderClient proveedorUbicacion;
    private ActivityResultLauncher<String[]> solicitudPermisosUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_espacios_educativos);

        textEstadoUbicacion = findViewById(R.id.textEstadoUbicacion);
        webViewMapa = findViewById(R.id.webViewMapa);
        btnVolverHome = findViewById(R.id.btnVolverHome);

        textEstadoUbicacion.setText("Preparando el mapa...");

        WebSettings configuracion = webViewMapa.getSettings();
        configuracion.setJavaScriptEnabled(true);
        configuracion.setDomStorageEnabled(true);

        webViewMapa.addJavascriptInterface(new MapaJavaScriptInterface(), "AndroidMapa");

        webViewMapa.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                textEstadoUbicacion.setText("Mapa cargado. Consultando espacios educativos...");

                cargarEspaciosEducativos();
                solicitarPermisosUbicacion();

            }

        });

        proveedorUbicacion = LocationServices.getFusedLocationProviderClient(this);

        solicitudPermisosUbicacion = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), resultados -> {

                            boolean precisa = Boolean.TRUE.equals(resultados.get(Manifest.permission.ACCESS_FINE_LOCATION));
                            boolean aproximada = Boolean.TRUE.equals(resultados.get(Manifest.permission.ACCESS_COARSE_LOCATION));

                            if (precisa || aproximada) {
                                obtenerUbicacion();

                            } else {
                                textEstadoUbicacion.setText("Permiso de ubicación denegado. Puedes consultar los espacios educativos sin compartir tu ubicación.");

                            }

                        });

        webViewMapa.loadUrl("file:///android_asset/mapa_espacios_educativos.html");
        btnVolverHome.setOnClickListener(v -> finish());

    }

    private void solicitarPermisosUbicacion() {

        boolean tieneUbicacionPrecisa = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean tieneUbicacionAproximada = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (tieneUbicacionPrecisa || tieneUbicacionAproximada) {

            obtenerUbicacion();

        } else {

            solicitudPermisosUbicacion.launch(new String[]{

                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION

                    }

            );

        }

    }

    private void obtenerUbicacion() {

        boolean tieneUbicacionPrecisa = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean tieneUbicacionAproximada = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!tieneUbicacionPrecisa && !tieneUbicacionAproximada) {

            textEstadoUbicacion.setText("No se concedieron los permisos de ubicación.");
            return;

        }

        textEstadoUbicacion.setText("Obteniendo ubicación actual...");
        com.google.android.gms.location.CurrentLocationRequest solicitud = new com.google.android.gms.location.CurrentLocationRequest.Builder()
                        .setPriority(tieneUbicacionPrecisa ? Priority.PRIORITY_HIGH_ACCURACY : Priority.PRIORITY_BALANCED_POWER_ACCURACY
                        ).setDurationMillis(15000).build();

        proveedorUbicacion.getCurrentLocation(solicitud, null).addOnSuccessListener(this, ubicacion -> {

                    if (ubicacion != null) {
                        mostrarUbicacionEstudiante(ubicacion);

                    } else {
                        textEstadoUbicacion.setText("No se pudo obtener la ubicación actual. " + "Comprueba la ubicación del emulador.");

                    }

                }).addOnFailureListener(this, error -> {

                    Log.e("HU15", "Error al obtener la ubicación actual", error);
                    textEstadoUbicacion.setText("Error al obtener la ubicación del dispositivo.");

                });

    }

    private void mostrarUbicacionEstudiante(Location ubicacion) {

        double latitud = ubicacion.getLatitude();
        double longitud = ubicacion.getLongitude();

        String codigoJavaScript = "mostrarUbicacionEstudiante(" + latitud + "," + longitud + ");";

        webViewMapa.post(() -> {

            webViewMapa.evaluateJavascript(codigoJavaScript, null);
            textEstadoUbicacion.setText("Tu ubicación se ha identificado correctamente.");

        });

    }



    private void cargarEspaciosEducativos() {

        ApiService apiService = RetrofitClient.getApiService();
        apiService.obtenerEspaciosEducativos().enqueue(new Callback<List<EspacioEducativoResponse>>() {

                    @Override
                    public void onResponse(Call<List<EspacioEducativoResponse>> call, Response<List<EspacioEducativoResponse>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            try {

                                org.json.JSONArray espaciosJson = new org.json.JSONArray();

                                for (EspacioEducativoResponse espacio : response.body()) {

                                    org.json.JSONObject objeto = new org.json.JSONObject();

                                    objeto.put("id", espacio.getId());
                                    objeto.put("nombre", espacio.getNombre());
                                    objeto.put("descripcion", espacio.getDescripcion());
                                    objeto.put("direccion", espacio.getDireccion());
                                    objeto.put("tipo", espacio.getTipo());
                                    objeto.put("latitud", espacio.getLatitud());
                                    objeto.put("longitud", espacio.getLongitud());

                                    espaciosJson.put(objeto);

                                }

                                String datosJson = espaciosJson.toString();

                                webViewMapa.post(() -> {

                                    webViewMapa.evaluateJavascript("recibirEspaciosEducativos(" + org.json.JSONObject.quote(datosJson) + ");",
                                            null);

                                });

                                textEstadoUbicacion.setText("Espacios educativos encontrados: " + response.body().size());

                            } catch (org.json.JSONException e) {

                                Log.e("HU15", "Error al preparar los espacios educativos", e);
                                textEstadoUbicacion.setText("No se pudieron procesar los espacios educativos.");

                            }

                        } else {

                            Log.e("HU15", "Error HTTP al consultar espacios: " + response.code());
                            textEstadoUbicacion.setText("No se pudieron cargar los espacios educativos.");

                        }

                    }

                    @Override
                    public void onFailure(Call<List<EspacioEducativoResponse>> call, Throwable t) {

                        Log.e("HU15", "Error de conexión con el backend", t);
                        textEstadoUbicacion.setText("No se pudo conectar con el servidor.");

                    }

                }

        );

    }

    private class MapaJavaScriptInterface {

        @JavascriptInterface
        public void mostrarEspacios(String datosJson) {

            runOnUiThread(() -> {

                try {

                    JSONArray espacios = new JSONArray(datosJson);
                    String datosSeguros = JSONObject.quote(espacios.toString());

                    webViewMapa.evaluateJavascript("recibirEspaciosEducativos(" + datosSeguros + ");", null);
                    textEstadoUbicacion.setText("Espacios recibidos: " + espacios.length());

                } catch (JSONException e) {

                    textEstadoUbicacion.setText("No se pudieron procesar los espacios educativos.");

                }

            });

        }

    }

}
