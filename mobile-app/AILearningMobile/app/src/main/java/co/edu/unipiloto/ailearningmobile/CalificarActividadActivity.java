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
import co.edu.unipiloto.ailearningmobile.dto.CalificacionResponse;
import co.edu.unipiloto.ailearningmobile.dto.CursoResponse;
import co.edu.unipiloto.ailearningmobile.dto.EstudianteCursoResponse;
import co.edu.unipiloto.ailearningmobile.dto.RegistrarCalificacionRequest;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalificarActividadActivity extends AppCompatActivity {

    private Spinner spinnerCursos;
    private Spinner spinnerActividades;
    private Spinner spinnerEstudiantes;

    private EditText editNota;
    private EditText editRetroalimentacion;

    private Button btnGuardarCalificacion;

    private Long usuarioId;

    private final List<CursoResponse> cursos = new ArrayList<>();
    private final List<ActividadResponse> actividades = new ArrayList<>();
    private final List<EstudianteCursoResponse> estudiantes = new ArrayList<>();

    private ArrayAdapter<String> adapterCursos;
    private ArrayAdapter<String> adapterActividades;
    private ArrayAdapter<String> adapterEstudiantes;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_actividad);

        usuarioId = getIntent().getLongExtra("usuarioId", -1L);

        spinnerCursos = findViewById(R.id.spinnerCursosCalificacion);
        spinnerActividades = findViewById(R.id.spinnerActividadesCalificacion);
        spinnerEstudiantes = findViewById(R.id.spinnerEstudiantesCalificacion);

        editNota = findViewById(R.id.editNota);
        editRetroalimentacion = findViewById(R.id.editRetroalimentacion);

        btnGuardarCalificacion =
                findViewById(R.id.btnGuardarCalificacion);

        apiService = RetrofitClient.getApiService();

        configurarSpinners();
        cargarCursos();

        btnGuardarCalificacion.setOnClickListener(
                v -> registrarCalificacion()
        );
    }

    private void configurarSpinners() {

        adapterCursos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );

        adapterActividades = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );

        adapterEstudiantes = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );

        adapterCursos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        adapterActividades.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        adapterEstudiantes.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCursos.setAdapter(adapterCursos);
        spinnerActividades.setAdapter(adapterActividades);
        spinnerEstudiantes.setAdapter(adapterEstudiantes);

        spinnerCursos.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        if (position >= 0 && position < cursos.size()) {

                            Long cursoId = cursos.get(position).getId();

                            cargarActividades(cursoId);
                            cargarEstudiantes(cursoId);
                        }
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }

    private void cargarCursos() {

        apiService.obtenerCursos()
                .enqueue(new Callback<List<CursoResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<CursoResponse>> call,
                            Response<List<CursoResponse>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            cursos.clear();
                            cursos.addAll(response.body());

                            adapterCursos.clear();

                            for (CursoResponse curso : cursos) {
                                adapterCursos.add(curso.getNombre());
                            }

                            adapterCursos.notifyDataSetChanged();

                        } else {

                            mostrarMensaje(
                                    "No se pudieron cargar los cursos."
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<CursoResponse>> call,
                            Throwable t) {

                        mostrarMensaje(
                                "Error de conexión al cargar cursos."
                        );
                    }
                });
    }

    private void cargarActividades(Long cursoId) {

        apiService.obtenerActividadesCurso(cursoId)
                .enqueue(new Callback<List<ActividadResponse>>() {

                    @Override
                    public void onResponse(
                            Call<List<ActividadResponse>> call,
                            Response<List<ActividadResponse>> response) {

                        actividades.clear();
                        adapterActividades.clear();

                        if (response.isSuccessful()
                                && response.body() != null) {

                            actividades.addAll(response.body());

                            for (ActividadResponse actividad : actividades) {

                                adapterActividades.add(
                                        actividad.getDescripcion()
                                                + " | "
                                                + actividad.getFecha()
                                );
                            }

                            adapterActividades.notifyDataSetChanged();

                            if (actividades.isEmpty()) {
                                mostrarMensaje(
                                        "Este curso no tiene actividades."
                                );
                            }

                        } else {

                            mostrarMensaje(
                                    "No se pudieron cargar las actividades."
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<ActividadResponse>> call,
                            Throwable t) {

                        mostrarMensaje(
                                "Error al cargar actividades."
                        );
                    }
                });
    }

    private void cargarEstudiantes(Long cursoId) {

        apiService.obtenerEstudiantesCurso(
                cursoId,
                usuarioId
        ).enqueue(new Callback<List<EstudianteCursoResponse>>() {

            @Override
            public void onResponse(
                    Call<List<EstudianteCursoResponse>> call,
                    Response<List<EstudianteCursoResponse>> response) {

                estudiantes.clear();
                adapterEstudiantes.clear();

                if (response.isSuccessful()
                        && response.body() != null) {

                    estudiantes.addAll(response.body());

                    for (EstudianteCursoResponse estudiante : estudiantes) {

                        adapterEstudiantes.add(
                                estudiante.getNombre()
                                        + " - "
                                        + estudiante.getCorreo()
                        );
                    }

                    adapterEstudiantes.notifyDataSetChanged();

                    if (estudiantes.isEmpty()) {

                        mostrarMensaje(
                                "No hay estudiantes inscritos en este curso."
                        );
                    }

                } else if (response.code() == 403) {

                    mostrarMensaje(
                            "No tienes permiso para consultar estudiantes."
                    );

                } else {

                    mostrarMensaje(
                            "No se pudieron cargar los estudiantes."
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<List<EstudianteCursoResponse>> call,
                    Throwable t) {

                mostrarMensaje(
                        "Error al cargar estudiantes."
                );
            }
        });
    }

    private void registrarCalificacion() {

        if (actividades.isEmpty()) {

            mostrarMensaje(
                    "Selecciona un curso con actividades."
            );
            return;
        }

        if (estudiantes.isEmpty()) {

            mostrarMensaje(
                    "No hay estudiantes disponibles para calificar."
            );
            return;
        }

        String notaTexto =
                editNota.getText().toString().trim();

        String retroalimentacion =
                editRetroalimentacion.getText().toString().trim();

        if (notaTexto.isEmpty()) {

            mostrarMensaje(
                    "Ingresa una calificación."
            );
            return;
        }

        BigDecimal nota;

        try {

            nota = new BigDecimal(
                    notaTexto.replace(",", ".")
            );

        } catch (NumberFormatException e) {

            mostrarMensaje(
                    "La calificación no es válida."
            );
            return;
        }

        ActividadResponse actividad =
                actividades.get(
                        spinnerActividades.getSelectedItemPosition()
                );

        EstudianteCursoResponse estudiante =
                estudiantes.get(
                        spinnerEstudiantes.getSelectedItemPosition()
                );

        RegistrarCalificacionRequest request =
                new RegistrarCalificacionRequest(
                        estudiante.getId(),
                        nota,
                        retroalimentacion
                );

        btnGuardarCalificacion.setEnabled(false);

        apiService.registrarCalificacion(
                actividad.getId(),
                usuarioId,
                request
        ).enqueue(new Callback<CalificacionResponse>() {

            @Override
            public void onResponse(
                    Call<CalificacionResponse> call,
                    Response<CalificacionResponse> response) {

                btnGuardarCalificacion.setEnabled(true);

                if (response.isSuccessful()
                        && response.body() != null) {

                    CalificacionResponse resultado =
                            response.body();

                    mostrarMensaje(
                            "Calificación guardada para "
                                    + resultado.getNombreEstudiante()
                                    + ": "
                                    + resultado.getNota()
                    );

                    editNota.setText("");
                    editRetroalimentacion.setText("");

                } else if (response.code() == 403) {

                    mostrarMensaje(
                            "No tienes permiso para registrar calificaciones."
                    );

                } else {

                    mostrarMensaje(
                            "No se pudo guardar la calificación. Código: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<CalificacionResponse> call,
                    Throwable t) {

                btnGuardarCalificacion.setEnabled(true);

                mostrarMensaje(
                        "Error de conexión con el servidor."
                );
            }
        });
    }

    private void mostrarMensaje(String mensaje) {

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
    }
}