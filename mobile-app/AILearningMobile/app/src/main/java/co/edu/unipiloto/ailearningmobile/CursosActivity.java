package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.view.View;
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
    private CursoAdapter cursoAdapter;
    private final List<CursoResponse> listaCursos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        recyclerCursos = findViewById(R.id.recyclerCursos);
        progressCursos = findViewById(R.id.progressCursos);
        tvMensajeCursos = findViewById(R.id.tvMensajeCursos);

        recyclerCursos.setLayoutManager(new LinearLayoutManager(this));

        cursoAdapter = new CursoAdapter(listaCursos);
        recyclerCursos.setAdapter(cursoAdapter);

        cargarCursos();

    }

    private void cargarCursos() {

        progressCursos.setVisibility(View.VISIBLE);
        tvMensajeCursos.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();

        apiService.obtenerCursos().enqueue(new Callback<List<CursoResponse>>() {

            @Override
            public void onResponse(Call<List<CursoResponse>> call,
                    Response<List<CursoResponse>> response) {

                progressCursos.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {

                    listaCursos.clear();
                    listaCursos.addAll(response.body());

                    cursoAdapter.notifyDataSetChanged();

                    if (listaCursos.isEmpty()) {

                        tvMensajeCursos.setText("No hay cursos disponibles.");
                        tvMensajeCursos.setVisibility(View.VISIBLE);

                    }} else {

                    tvMensajeCursos.setText("No se pudieron cargar los cursos.");
                    tvMensajeCursos.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<CursoResponse>> call, Throwable t) {

                progressCursos.setVisibility(View.GONE);

                tvMensajeCursos.setText("Error de conexión con el servidor.");
                tvMensajeCursos.setVisibility(View.VISIBLE);

                Toast.makeText(CursosActivity.this, "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }

        });

    }

}