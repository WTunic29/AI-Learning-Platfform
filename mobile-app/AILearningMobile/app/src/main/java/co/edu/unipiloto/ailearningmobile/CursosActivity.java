package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.edu.unipiloto.ailearningmobile.adapter.CursoAdapter;
import co.edu.unipiloto.ailearningmobile.dto.CursoResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosActivity extends AppCompatActivity {

    private RecyclerView recyclerCursos;
    private ProgressBar progressCursos;
    private TextView tvMensajeCursos;
    private EditText editBuscarCurso;
    private CursoAdapter cursoAdapter;

    private final List<CursoResponse> listaCursos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        recyclerCursos = findViewById(R.id.recyclerCursos);
        progressCursos = findViewById(R.id.progressCursos);
        tvMensajeCursos = findViewById(R.id.tvMensajeCursos);
        editBuscarCurso = findViewById(R.id.editBuscarCurso);

        Button btnBuscarCurso = findViewById(R.id.btnBuscarCurso);
        Button btnMostrarTodos = findViewById(R.id.btnMostrarTodos);

        recyclerCursos.setLayoutManager(new LinearLayoutManager(this));

        cursoAdapter = new CursoAdapter(listaCursos);
        recyclerCursos.setAdapter(cursoAdapter);

        btnBuscarCurso.setOnClickListener(v -> buscarCursos());

        btnMostrarTodos.setOnClickListener(v -> {
            editBuscarCurso.setText("");
            cargarCursos();
        });

        cargarCursos();
    }

    private void cargarCursos() {

        mostrarCargando();

        ApiService apiService = RetrofitClient.getApiService();

        apiService.obtenerCursos().enqueue(new Callback<List<CursoResponse>>() {

            @Override
            public void onResponse(
                    Call<List<CursoResponse>> call,
                    Response<List<CursoResponse>> response) {

                procesarRespuesta(response, "No hay cursos disponibles.");
            }

            @Override
            public void onFailure(
                    Call<List<CursoResponse>> call,
                    Throwable t) {

                mostrarError("Error de conexión con el servidor.");
            }
        });
    }

    private void buscarCursos() {

        String termino = editBuscarCurso.getText().toString().trim();

        if (termino.isEmpty()) {
            cargarCursos();
            return;
        }

        mostrarCargando();

        ApiService apiService = RetrofitClient.getApiService();

        apiService.buscarCursos(termino).enqueue(new Callback<List<CursoResponse>>() {

            @Override
            public void onResponse(
                    Call<List<CursoResponse>> call,
                    Response<List<CursoResponse>> response) {

                procesarRespuesta(
                        response,
                        "No se encontraron cursos para: " + termino
                );
            }

            @Override
            public void onFailure(
                    Call<List<CursoResponse>> call,
                    Throwable t) {

                mostrarError("Error de conexión con el servidor.");
            }
        });
    }

    private void procesarRespuesta(
            Response<List<CursoResponse>> response,
            String mensajeVacio) {

        progressCursos.setVisibility(View.GONE);
        tvMensajeCursos.setVisibility(View.GONE);

        if (response.isSuccessful() && response.body() != null) {

            listaCursos.clear();
            listaCursos.addAll(response.body());

            cursoAdapter.notifyDataSetChanged();

            if (listaCursos.isEmpty()) {
                tvMensajeCursos.setText(mensajeVacio);
                tvMensajeCursos.setVisibility(View.VISIBLE);
            }

        } else {

            mostrarError(
                    "No se pudieron cargar los cursos. Código: " +
                    response.code()
            );
        }
    }

    private void mostrarCargando() {

        progressCursos.setVisibility(View.VISIBLE);
        tvMensajeCursos.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {

        progressCursos.setVisibility(View.GONE);
        tvMensajeCursos.setText(mensaje);
        tvMensajeCursos.setVisibility(View.VISIBLE);

        Toast.makeText(
                CursosActivity.this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
    }
}
