package co.edu.unipiloto.ailearningmobile;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.CertificadoResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificadoActivity extends AppCompatActivity {

    private Long usuarioId;
    private Long cursoId;
    private LinearLayout contenedorCertificados;
    private TextView txtEstadoCertificados;
    private TextView txtResultado;
    private Button btnGenerar;
    private ApiService api;
    private CertificadoResponse certificadoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificado);

        usuarioId = getIntent().getLongExtra("usuarioId", -1L);
        cursoId = getIntent().getLongExtra("cursoId", 1L);

        txtResultado = findViewById(R.id.txtResultadoCertificado);
        btnGenerar = findViewById(R.id.btnGenerarCertificado);
        contenedorCertificados = findViewById(R.id.contenedorCertificados);
        txtEstadoCertificados = findViewById(R.id.txtEstadoCertificados);

        api = RetrofitClient.getApiService();

        btnGenerar.setOnClickListener(v -> generarCertificado());

        if (usuarioId > 0) {
            obtenerCertificados();
        } else {
            txtResultado.setText("No se pudo identificar al estudiante. Inicia sesión nuevamente.");
        }

    }

    private void obtenerCertificados() {

        txtEstadoCertificados.setText("Consultando certificados...");

        api.obtenerCertificados(usuarioId).enqueue(new Callback<List<CertificadoResponse>>() {

                    @Override
                    public void onResponse(Call<List<CertificadoResponse>> call, Response<List<CertificadoResponse>> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            txtEstadoCertificados.setText("No se pudieron consultar los certificados.");
                            return;
                        }

                        List<CertificadoResponse> certificados = response.body();
                        contenedorCertificados.removeAllViews();

                        if (certificados.isEmpty()) {

                            txtEstadoCertificados.setText("Todavía no tienes certificados disponibles.");
                            certificadoActual = null;
                            return;

                        }

                        txtEstadoCertificados.setText("Certificados obtenidos: " + certificados.size());

                        for (CertificadoResponse certificado : certificados) {

                            LinearLayout tarjeta = new LinearLayout(CertificadoActivity.this);
                            tarjeta.setOrientation(LinearLayout.VERTICAL);
                            tarjeta.setPadding(24, 24, 24, 24);

                            LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                            parametros.setMargins(0, 0, 0, 24);
                            tarjeta.setLayoutParams(parametros);

                            TextView informacion = new TextView(CertificadoActivity.this);

                            informacion.setText("Curso: " + certificado.getNombreCurso() + "\nEstudiante: " + certificado.getNombreEstudiante()
                                    + "\nPromedio: " + certificado.getPromedio() + "\nCódigo: " + certificado.getCodigoVerificacion());
                            informacion.setTextSize(16);

                            Button botonDescargar = new Button(CertificadoActivity.this);
                            botonDescargar.setText("Descargar PDF");
                            botonDescargar.setAllCaps(false);

                            botonDescargar.setOnClickListener(v -> {

                                certificadoActual = certificado;
                                descargarCertificado();

                            });

                            tarjeta.addView(informacion);
                            tarjeta.addView(botonDescargar);
                            contenedorCertificados.addView(tarjeta);

                        }

                        certificadoActual = certificados.get(0);

                    }

                    @Override
                    public void onFailure(Call<List<CertificadoResponse>> call, Throwable t) {

                        txtEstadoCertificados.setText("Error de conexión con el servidor.");
                        Toast.makeText(CertificadoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

                    }

                });

    }

    private void generarCertificado() {

        api.generarCertificado(usuarioId, cursoId)
                .enqueue(new Callback<CertificadoResponse>() {

                    @Override
                    public void onResponse(
                            Call<CertificadoResponse> call,
                            Response<CertificadoResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            certificadoActual = response.body();

                            txtResultado.setText(
                                    "CERTIFICADO DISPONIBLE\n\n"
                                    + "Estudiante: " + certificadoActual.getNombreEstudiante()
                                    + "\nCurso: " + certificadoActual.getNombreCurso()
                                    + "\nPromedio: " + certificadoActual.getPromedio()
                                    + "\nCódigo: " + certificadoActual.getCodigoVerificacion()
                            );


                            Toast.makeText(
                                    CertificadoActivity.this,
                                    "Certificado generado correctamente",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else if (response.code() == 409) {

                            Toast.makeText(
                                    CertificadoActivity.this,
                                    "Aún no cumples los requisitos para certificarte",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Toast.makeText(
                                    CertificadoActivity.this,
                                    "Error: " + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<CertificadoResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                CertificadoActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void descargarCertificado() {

        if (certificadoActual == null) {
            return;
        }

        api.descargarCertificado(
                certificadoActual.getId(),
                usuarioId
        ).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response) {

                if (!response.isSuccessful() || response.body() == null) {

                    Toast.makeText(
                            CertificadoActivity.this,
                            "No se pudo descargar el PDF",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                guardarPdf(response.body());
            }

            @Override
            public void onFailure(
                    Call<ResponseBody> call,
                    Throwable t) {

                Toast.makeText(
                        CertificadoActivity.this,
                        "Error al descargar",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void guardarPdf(ResponseBody body) {

        try {

            String nombre =
                    "Certificado_NexaLearn_"
                    + certificadoActual.getId()
                    + ".pdf";

            ContentValues values = new ContentValues();

            values.put(
                    MediaStore.Downloads.DISPLAY_NAME,
                    nombre
            );

            values.put(
                    MediaStore.Downloads.MIME_TYPE,
                    "application/pdf"
            );

            values.put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                            + "/NexaLearn"
            );

            var uri = getContentResolver().insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
            );

            if (uri == null) {
                return;
            }

            try (OutputStream output =
                         getContentResolver().openOutputStream(uri)) {

                if (output != null) {
                    output.write(body.bytes());
                }
            }

            Toast.makeText(
                    this,
                    "PDF guardado en Descargas/NexaLearn",
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error guardando PDF",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}