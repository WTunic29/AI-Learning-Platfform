package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.ActividadResponse;
import co.edu.unipiloto.ailearningmobile.dto.CrearActividadRequest;
import co.edu.unipiloto.ailearningmobile.dto.CursoResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CrearActividadActivity extends AppCompatActivity{

    private Spinner spinnerCursos;
    private Long usuarioId;
    private EditText editDescripcionActividad;
    private EditText editFechaActividad;
    private EditText editPonderacionActividad;
    private Button btnCrearActividad;
    private final List<CursoResponse> listaCursos = new ArrayList<>();
    private ArrayAdapter<String> adapterCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        usuarioId = getIntent().getLongExtra("usuarioId", -1L);

        spinnerCursos = findViewById(R.id.spinnerCursos);
        editDescripcionActividad = findViewById(R.id.editDescripcionActividad);
        editFechaActividad = findViewById(R.id.editFechaActividad);
        editPonderacionActividad = findViewById(R.id.editPonderacionActividad);
        btnCrearActividad = findViewById(R.id.btnCrearActividad);

        adapterCursos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCursos.setAdapter(adapterCursos);

        cargarCursos();
        btnCrearActividad.setOnClickListener(v -> crearActividad());
    }

    private void cargarCursos() {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.obtenerCursos().enqueue(new Callback<List<CursoResponse>>() {

                    @Override
                    public void onResponse(Call<List<CursoResponse>> call, Response<List<CursoResponse>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            listaCursos.clear();
                            listaCursos.addAll(response.body());

                            adapterCursos.clear();

                            for (CursoResponse curso : listaCursos) {
                                adapterCursos.add(curso.getNombre());
                            }

                            adapterCursos.notifyDataSetChanged();

                        } else {

                            Toast.makeText(CrearActividadActivity.this, "No se pudieron cargar los cursos. Código: "
                                    + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CursoResponse>> call, Throwable t) {
                        Toast.makeText(CrearActividadActivity.this, "Error de conexión con el servidor.",
                                Toast.LENGTH_LONG).show();
                    }

                });

    }

    private void crearActividad() {

        if (listaCursos.isEmpty()) {

            Toast.makeText(this, "No hay cursos disponibles.", Toast.LENGTH_LONG).show();
            return;
        }

        String descripcion = editDescripcionActividad.getText().toString().trim();
        String fecha = editFechaActividad.getText().toString().trim();
        String ponderacionTexto = editPonderacionActividad.getText().toString().trim();

        if (descripcion.isEmpty() || fecha.isEmpty() || ponderacionTexto.isEmpty()) {

            Toast.makeText(this, "Complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;

        }

        BigDecimal ponderacion;

        try {

            ponderacion = new BigDecimal(ponderacionTexto);

        } catch (NumberFormatException e) {

            Toast.makeText(this, "La ponderación no es válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        CursoResponse cursoSeleccionado = listaCursos.get(spinnerCursos.getSelectedItemPosition());
        CrearActividadRequest request = new CrearActividadRequest(descripcion, fecha, ponderacion);

        ApiService apiService = RetrofitClient.getApiService();
        btnCrearActividad.setEnabled(false);

        apiService.crearActividad(cursoSeleccionado.getId(),usuarioId, request).enqueue(new Callback<ActividadResponse>() {

                    @Override
                    public void onResponse(Call<ActividadResponse> call, Response<ActividadResponse> response) {

                        btnCrearActividad.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {

                            Toast.makeText(CrearActividadActivity.this, "Actividad creada correctamente.",
                                    Toast.LENGTH_LONG).show();

                            limpiarFormulario();

                        } else {

                            Toast.makeText(CrearActividadActivity.this, "No se pudo crear la actividad. Código: "
                                            + response.code(), Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<ActividadResponse> call, Throwable t) {

                        btnCrearActividad.setEnabled(true);
                        Toast.makeText(CrearActividadActivity.this, "Error de conexión con el servidor.",
                                Toast.LENGTH_LONG).show();

                    }

                });

    }

    private void limpiarFormulario() {

        editDescripcionActividad.setText("");
        editFechaActividad.setText("");
        editPonderacionActividad.setText("");

        if (adapterCursos.getCount() > 0) {
            spinnerCursos.setSelection(0);
        }

    }

}
