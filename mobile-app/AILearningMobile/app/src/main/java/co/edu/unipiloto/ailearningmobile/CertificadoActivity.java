package co.edu.unipiloto.ailearningmobile;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;

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

    private TextView txtResultado;
    private Button btnGenerar;
    private Button btnDescargar;

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
        btnDescargar = findViewById(R.id.btnDescargarCertificado);

        api = RetrofitClient.getApiService();

        btnDescargar.setEnabled(false);

        btnGenerar.setOnClickListener(v -> generarCertificado());
        btnDescargar.setOnClickListener(v -> descargarCertificado());
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

                            btnDescargar.setEnabled(true);

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