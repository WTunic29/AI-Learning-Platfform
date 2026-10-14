package co.edu.unipiloto.ailearningmobile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.CursoProgresoResumenResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisCursosActivity extends AppCompatActivity {

    private long usuarioId;

    private LinearLayout contenedorFinalizados;
    private LinearLayout contenedorEnProgreso;

    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cursos);

        usuarioId = getIntent()
                .getLongExtra("usuarioId", -1L);

        contenedorFinalizados =
                findViewById(R.id.contenedorFinalizados);

        contenedorEnProgreso =
                findViewById(R.id.contenedorEnProgreso);

        api = RetrofitClient.getApiService();

        cargarCursos();
    }

    private void cargarCursos() {

        api.obtenerMisCursos(usuarioId)
                .enqueue(
                        new Callback<List<CursoProgresoResumenResponse>>() {

            @Override
            public void onResponse(
                    Call<List<CursoProgresoResumenResponse>> call,
                    Response<List<CursoProgresoResumenResponse>> response) {

                if (!response.isSuccessful()
                        || response.body() == null) {

                    Toast.makeText(
                            MisCursosActivity.this,
                            "No fue posible cargar tus cursos.",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                contenedorFinalizados.removeAllViews();
                contenedorEnProgreso.removeAllViews();

                for (CursoProgresoResumenResponse curso
                        : response.body()) {

                    agregarCurso(curso);
                }
            }

            @Override
            public void onFailure(
                    Call<List<CursoProgresoResumenResponse>> call,
                    Throwable t) {

                Toast.makeText(
                        MisCursosActivity.this,
                        "Error de conexiÃƒÂ³n con el servidor.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void agregarCurso(
            CursoProgresoResumenResponse curso) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                32,
                28,
                32,
                28
        );

        card.setBackgroundResource(
                R.drawable.bg_curso_progreso
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                24
        );

        card.setLayoutParams(params);

        TextView nombre =
                new TextView(this);

        nombre.setText(
                curso.getNombreCurso()
        );

        nombre.setTextSize(19);
        nombre.setTextColor(
                Color.parseColor("#1A2B49")
        );

        nombre.setTypeface(
                null,
                Typeface.BOLD
        );

        TextView porcentaje =
                new TextView(this);

        int avance =
                (int) Math.round(
                        curso.getPorcentajeAvance()
                );

        porcentaje.setText(
                "Avance del curso: "
                        + avance
                        + "%"
        );

        porcentaje.setTextSize(15);
        porcentaje.setTextColor(
                Color.parseColor("#536273")
        );

        porcentaje.setPadding(
                0,
                16,
                0,
                10
        );

        ProgressBar barra =
                new ProgressBar(
                        this,
                        null,
                        android.R.attr.progressBarStyleHorizontal
                );

        barra.setMax(100);
barra.setProgress(avance);
barra.setProgressDrawable(
        getResources().getDrawable(
                R.drawable.progress_curso,
                getTheme()
        )
);

        LinearLayout.LayoutParams barraParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        24
                );

        barraParams.setMargins(
                0,
                0,
                0,
                14
        );

        barra.setLayoutParams(barraParams);

        TextView estado =
                new TextView(this);

        boolean finalizado =
                "FINALIZADO".equalsIgnoreCase(
                        curso.getEstado()
                );

        if (finalizado) {

            estado.setText(
                    "\u2713 Curso finalizado"
            );

            estado.setTextColor(
                    Color.parseColor("#168447")
            );

        } else {

            estado.setText(
                    "En progreso"
            );

            estado.setTextColor(
                    Color.parseColor("#2667C9")
            );
        }

        estado.setTypeface(null, Typeface.BOLD);

        card.addView(nombre);
        card.addView(porcentaje);
        card.addView(barra);
        card.addView(estado);

        card.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MisCursosActivity.this,
                            ContenidoCursoActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            intent.putExtra(
                    "cursoId",
                    curso.getCursoId()
            );

            startActivity(intent);
        });

        if (finalizado) {

            contenedorFinalizados
                    .addView(card);

        } else {

            contenedorEnProgreso
                    .addView(card);
        }
    }
}