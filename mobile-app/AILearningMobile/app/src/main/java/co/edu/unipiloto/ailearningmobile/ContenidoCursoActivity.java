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
    private TextView tvProgresoCurso;
    private co.edu.unipiloto.ailearningmobile.dto.ProgresoCursoResponse progresoActual;
    private long usuarioId;
    private long cursoId;
    private List<ModuloResponse> modulosActuales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_curso);

        contenedorModulos = findViewById(R.id.contenedorModulos);
        progressContenido = findViewById(R.id.progressContenido);
        tvMensajeContenido = findViewById(R.id.tvMensajeContenido);
        tvTituloContenido = findViewById(R.id.tvTituloContenido);
        tvProgresoCurso = findViewById(R.id.tvProgresoCurso);

        usuarioId = getIntent().getLongExtra("usuarioId", -1L);
        cursoId = getIntent().getLongExtra("cursoId", -1L);
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
                    public void onResponse(Call<List<ModuloResponse>> call, Response<List<ModuloResponse>> response) {

                        progressContenido.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().isEmpty()) {
                                mostrarError("Este curso todavía no tiene contenido disponible.");
                                return;
                            }

                            modulosActuales = response.body();

                            mostrarModulos(response.body());
                            cargarProgresoCurso();

                        } else if (response.code() == 403) {

                            mostrarError("Debes estar inscrito en este curso para acceder al contenido.");

                        } else if (response.code() == 404) {

                            mostrarError("El usuario o el curso no existe.");

                        } else {

                            mostrarError("No se pudo cargar el contenido. Código: " + response.code());

                        }

                    }

                    @Override
                    public void onFailure(Call<List<ModuloResponse>> call, Throwable t) {
                        mostrarError("Error de conexión con el servidor.");
                    }

                });

    }

    private void cargarProgresoCurso() {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.obtenerProgresoCurso(usuarioId, cursoId).enqueue(new Callback<co.edu.unipiloto.ailearningmobile.dto.ProgresoCursoResponse>() {

                    @Override
                    public void onResponse(Call<co.edu.unipiloto.ailearningmobile.dto.ProgresoCursoResponse> call,
                                           Response<co.edu.unipiloto.ailearningmobile.dto.ProgresoCursoResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            // Guardar el progreso más reciente.
                            progresoActual = response.body();
                            // Actualizar el porcentaje.
                            double porcentaje = progresoActual.getPorcentajeAvance();

                            tvProgresoCurso.setText(String.format(java.util.Locale.getDefault(),
                                            "Avance del curso: %.2f%%", porcentaje));

                            // Actualizar los estados de los módulos.
                            if (modulosActuales != null) {
                                mostrarModulos(modulosActuales);
                                cargarMisCalificaciones();
                            }

                        } else {

                            tvProgresoCurso.setText("No se pudo consultar el progreso.");
                        }

                    }

                    @Override
                    public void onFailure(Call<co.edu.unipiloto.ailearningmobile.dto.ProgresoCursoResponse> call, Throwable t) {

                        tvProgresoCurso.setText("Error al consultar el progreso.");

                    }

                });
    }

    private boolean estaCompletado(Long moduloId) {

        if (progresoActual == null || progresoActual.getModulos() == null) {
            return false;
        }

        for (co.edu.unipiloto.ailearningmobile.dto.ModuloProgresoResponse modulo : progresoActual.getModulos()) {

            if (moduloId.equals(modulo.getModuloId())) {
                return modulo.isCompletado();
            }

        }

        return false;
    }

    private void mostrarModulos(List<ModuloResponse> modulos) {

        contenedorModulos.removeAllViews();

        for (ModuloResponse modulo : modulos) {

            // Título del módulo
            TextView tituloModulo = new TextView(this);

            tituloModulo.setText("Módulo " + modulo.getOrden() + ": " + modulo.getTitulo());
            tituloModulo.setTextSize(20);
            tituloModulo.setPadding(0, 24, 0, 8);

            contenedorModulos.addView(tituloModulo);

            // Descripción del módulo
            TextView descripcionModulo = new TextView(this);

            descripcionModulo.setText(modulo.getDescripcion());
            descripcionModulo.setTextSize(15);
            descripcionModulo.setPadding(0, 0, 0, 12);

            contenedorModulos.addView(descripcionModulo);

            // Consultar el estado real del módulo
            boolean completado = estaCompletado(modulo.getId());

            TextView tvEstadoModulo = new TextView(this);

            tvEstadoModulo.setText(completado ? "Estado: Completado" : "Estado: Pendiente");
            tvEstadoModulo.setTextSize(15);
            tvEstadoModulo.setPadding(0, 0, 0, 8);

            contenedorModulos.addView(tvEstadoModulo);

            // Mostrar el botón solamente si está pendiente
            if (!completado) {

                Button botonCompletar = new Button(this);
                botonCompletar.setText("Marcar como completado");
                botonCompletar.setAllCaps(false);

                botonCompletar.setOnClickListener(v -> marcarModuloCompletado(modulo.getId()));
                contenedorModulos.addView(botonCompletar);

            }

            // Recursos del módulo
            List<RecursoResponse> recursos = modulo.getRecursos();

            if (recursos == null || recursos.isEmpty()) {

                TextView sinRecursos = new TextView(this);

                sinRecursos.setText("Sin recursos disponibles.");
                sinRecursos.setPadding(16, 4, 0, 12);

                contenedorModulos.addView(sinRecursos);

            } else {

                for (RecursoResponse recurso : recursos) {

                    Button botonRecurso = new Button(this);

                    botonRecurso.setText(recurso.getTipo() + " · " + recurso.getTitulo());
                    botonRecurso.setAllCaps(false);
                    botonRecurso.setOnClickListener(v -> abrirRecurso(recurso.getUrl()));

                    contenedorModulos.addView(botonRecurso);

                }

            }

        }

    }

    private void marcarModuloCompletado(Long moduloId) {

        ApiService apiService = RetrofitClient.getApiService();
        apiService.marcarModuloCompletado(usuarioId, moduloId).enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(ContenidoCursoActivity.this, "Módulo completado correctamente.",
                                    Toast.LENGTH_SHORT).show();

                            cargarProgresoCurso();

                        } else {

                            Toast.makeText(ContenidoCursoActivity.this, "No se pudo completar el módulo. Código: " + response.code(),
                                    Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                        Toast.makeText(ContenidoCursoActivity.this,
                                "Error de conexión al guardar el progreso.", Toast.LENGTH_LONG).show();

                    }

                });

    }

    private void abrirRecurso(String url) {

        if (url == null || url.trim().isEmpty()) {

            Toast.makeText(this, "El recurso no tiene una URL disponible.", Toast.LENGTH_LONG).show();
            return;

        }

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(this, "No fue posible abrir el recurso.", Toast.LENGTH_LONG).show();
        }

    }

    private void mostrarError(String mensaje) {

        progressContenido.setVisibility(View.GONE);
        tvMensajeContenido.setText(mensaje);
        tvMensajeContenido.setVisibility(View.VISIBLE);

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

    }

    private void cargarMisCalificaciones() {

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService.obtenerMisCalificaciones(
                usuarioId,
                cursoId
        ).enqueue(
                new Callback<List<co.edu.unipiloto.ailearningmobile.dto.MiCalificacionResponse>>() {

            @Override
            public void onResponse(
                    Call<List<co.edu.unipiloto.ailearningmobile.dto.MiCalificacionResponse>> call,
                    Response<List<co.edu.unipiloto.ailearningmobile.dto.MiCalificacionResponse>> response) {

                if (!response.isSuccessful()
                        || response.body() == null) {
                    return;
                }

                List<co.edu.unipiloto.ailearningmobile.dto.MiCalificacionResponse>
                        calificaciones = response.body();

                TextView titulo =
                        new TextView(
                                ContenidoCursoActivity.this
                        );

                titulo.setText("Mis calificaciones");
                titulo.setTextSize(22);
                titulo.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                );

                titulo.setPadding(
                        0,
                        40,
                        0,
                        20
                );

                contenedorModulos.addView(titulo);

                if (calificaciones.isEmpty()) {

                    TextView sinNotas =
                            new TextView(
                                    ContenidoCursoActivity.this
                            );

                    sinNotas.setText(
                            "Aun no tienes actividades calificadas."
                    );

                    sinNotas.setTextSize(15);

                    contenedorModulos.addView(sinNotas);

                    return;
                }

                double sumaPonderada = 0.0;
                double totalPeso = 0.0;

                for (co.edu.unipiloto.ailearningmobile.dto.MiCalificacionResponse calificacion
                        : calificaciones) {

                    LinearLayout tarjeta =
                            new LinearLayout(
                                    ContenidoCursoActivity.this
                            );

                    tarjeta.setOrientation(
                            LinearLayout.VERTICAL
                    );

                    tarjeta.setPadding(
                            28,
                            24,
                            28,
                            24
                    );

                    tarjeta.setBackgroundResource(
                            R.drawable.bg_curso_progreso
                    );

                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                    params.setMargins(
                            0,
                            0,
                            0,
                            20
                    );

                    tarjeta.setLayoutParams(params);

                    TextView actividad =
                            new TextView(
                                    ContenidoCursoActivity.this
                            );

                    actividad.setText(
                            calificacion.getActividad()
                    );

                    actividad.setTextSize(17);

                    actividad.setTypeface(
                            null,
                            android.graphics.Typeface.BOLD
                    );

                    TextView nota =
                            new TextView(
                                    ContenidoCursoActivity.this
                            );

                    nota.setText(
                            "Nota: "
                                    + calificacion.getNota()
                                    + " / 5.0"
                    );

                    nota.setTextSize(16);

                    nota.setPadding(
                            0,
                            10,
                            0,
                            6
                    );

                    TextView retro =
                            new TextView(
                                    ContenidoCursoActivity.this
                            );

                    String comentario =
                            calificacion.getRetroalimentacion();

                    if (comentario == null
                            || comentario.trim().isEmpty()) {

                        comentario =
                                "Sin retroalimentacion.";
                    }

                    retro.setText(
                            "Retroalimentacion: "
                                    + comentario
                    );

                    retro.setTextSize(14);

                    tarjeta.addView(actividad);
                    tarjeta.addView(nota);
                    tarjeta.addView(retro);

                    contenedorModulos.addView(tarjeta);

                    double peso = 1.0;

                    if (calificacion.getPonderacion() != null
                            && calificacion
                            .getPonderacion()
                            .doubleValue() > 0) {

                        peso =
                                calificacion
                                .getPonderacion()
                                .doubleValue();
                    }

                    sumaPonderada +=
                            calificacion
                            .getNota()
                            .doubleValue()
                            * peso;

                    totalPeso += peso;
                }

                if (totalPeso > 0) {

                    double promedio =
                            sumaPonderada
                                    / totalPeso;

                    TextView resumen =
                            new TextView(
                                    ContenidoCursoActivity.this
                            );

                    resumen.setText(
                            String.format(
                                    java.util.Locale.getDefault(),
                                    "Promedio: %.2f / 5.0\nEstado: %s",
                                    promedio,
                                    promedio >= 3.0
                                            ? "APROBADO"
                                            : "NO APROBADO"
                            )
                    );

                    resumen.setTextSize(18);

                    resumen.setTypeface(
                            null,
                            android.graphics.Typeface.BOLD
                    );

                    resumen.setPadding(
                            0,
                            10,
                            0,
                            30
                    );

                    contenedorModulos.addView(resumen);
                }
            }

            @Override
            public void onFailure(
                    Call<List<co.edu.unipiloto.ailearningmobile.dto.MiCalificacionResponse>> call,
                    Throwable t) {
            }
        });
    }

}