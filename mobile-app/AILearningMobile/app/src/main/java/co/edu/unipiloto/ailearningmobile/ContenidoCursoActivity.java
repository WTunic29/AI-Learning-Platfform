package co.edu.unipiloto.ailearningmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.ModuloResponse;
import co.edu.unipiloto.ailearningmobile.dto.RecursoResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContenidoCursoActivity extends AppCompatActivity {

    private LinearLayout contenedorModulos;
    private ProgressBar progressContenido;
    private TextView tvMensajeContenido;
    private TextView tvTituloContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_curso);

        contenedorModulos = findViewById(R.id.contenedorModulos);
        progressContenido = findViewById(R.id.progressContenido);
        tvMensajeContenido = findViewById(R.id.tvMensajeContenido);
        tvTituloContenido = findViewById(R.id.tvTituloContenido);

        long usuarioId = getIntent().getLongExtra("usuarioId", -1L);
        long cursoId = getIntent().getLongExtra("cursoId", -1L);
        String nombreCurso = getIntent().getStringExtra("nombreCurso");

        if (nombreCurso != null && !nombreCurso.trim().isEmpty()) {
            tvTituloContenido.setText(nombreCurso);
        }

        if (usuarioId == -1L || cursoId == -1L) {
            mostrarError("No fue posible identificar el usuario o el curso.");
            return;
        }

        cargarContenido(usuarioId, cursoId);
    }

    private void cargarContenido(long usuarioId, long cursoId) {

        progressContenido.setVisibility(View.VISIBLE);
        tvMensajeContenido.setVisibility(View.GONE);
        contenedorModulos.removeAllViews();

        ApiService apiService = RetrofitClient.getApiService();

        apiService.obtenerContenidoCurso(usuarioId, cursoId)
                .enqueue(new Callback<List<ModuloResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<ModuloResponse>> call,
                            Response<List<ModuloResponse>> response) {

                        progressContenido.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().isEmpty()) {
                                mostrarError("Este curso todavía no tiene contenido disponible.");
                                return;
                            }

                            mostrarModulos(response.body());

                        } else if (response.code() == 403) {

                            mostrarError(
                                    "Debes estar inscrito en este curso para acceder al contenido."
                            );

                        } else if (response.code() == 404) {

                            mostrarError("El usuario o el curso no existe.");

                        } else {

                            mostrarError(
                                    "No se pudo cargar el contenido. Código: " +
                                            response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<ModuloResponse>> call,
                            Throwable t) {

                        mostrarError(
                                "Error de conexión con el servidor."
                        );
                    }
                });
    }

    private void mostrarModulos(List<ModuloResponse> modulos) {

        contenedorModulos.removeAllViews();

        for (ModuloResponse modulo : modulos) {

            TextView tituloModulo = new TextView(this);
            tituloModulo.setText(
                    "Módulo " + modulo.getOrden() + ": " +
                            modulo.getTitulo()
            );
            tituloModulo.setTextSize(20);
            tituloModulo.setPadding(0, 24, 0, 8);

            contenedorModulos.addView(tituloModulo);

            TextView descripcionModulo = new TextView(this);
            descripcionModulo.setText(modulo.getDescripcion());
            descripcionModulo.setTextSize(15);
            descripcionModulo.setPadding(0, 0, 0, 12);

            contenedorModulos.addView(descripcionModulo);

            List<RecursoResponse> recursos = modulo.getRecursos();

            if (recursos == null || recursos.isEmpty()) {

                TextView sinRecursos = new TextView(this);
                sinRecursos.setText("Sin recursos disponibles.");
                sinRecursos.setPadding(16, 4, 0, 12);

                contenedorModulos.addView(sinRecursos);
                continue;
            }

            for (RecursoResponse recurso : recursos) {

                Button botonRecurso = new Button(this);

                botonRecurso.setText(
                        recurso.getTipo() + " · " +
                                recurso.getTitulo()
                );

                botonRecurso.setAllCaps(false);

                botonRecurso.setOnClickListener(v ->
                        abrirRecurso(recurso.getUrl())
                );

                contenedorModulos.addView(botonRecurso);
            }
        }
    }

    private void abrirRecurso(String url) {

        if (url == null || url.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "El recurso no tiene una URL disponible.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        try {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "No fue posible abrir el recurso.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void mostrarError(String mensaje) {

        progressContenido.setVisibility(View.GONE);
        tvMensajeContenido.setText(mensaje);
        tvMensajeContenido.setVisibility(View.VISIBLE);

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
    }
}