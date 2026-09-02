package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import co.edu.unipiloto.ailearningmobile.dto.CambiarRolRequest;
import co.edu.unipiloto.ailearningmobile.dto.UsuarioResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarUsuarioActivity extends AppCompatActivity {

    private EditText editBuscar;
    private LinearLayout contenedorResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_usuario);

        editBuscar = findViewById(R.id.editBuscar);
        Button btnBuscar = findViewById(R.id.btnBuscar);
        contenedorResultados = findViewById(R.id.contenedorResultados);

        btnBuscar.setOnClickListener(v -> buscarUsuarios());
    }

    private void buscarUsuarios() {

        String termino = editBuscar.getText().toString().trim();

        if (termino.isEmpty()) {
            Toast.makeText(this,
                    "Ingrese un nombre o correo",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();

        apiService.buscarUsuarios(termino).enqueue(new Callback<List<UsuarioResponse>>() {

            @Override
            public void onResponse(
                    Call<List<UsuarioResponse>> call,
                    Response<List<UsuarioResponse>> response) {

                contenedorResultados.removeAllViews();

                if (!response.isSuccessful() || response.body() == null) {
                    mostrarMensaje("Error al buscar usuarios");
                    return;
                }

                List<UsuarioResponse> usuarios = response.body();

                if (usuarios.isEmpty()) {
                    mostrarMensaje("No se encontraron usuarios");
                    return;
                }

                for (UsuarioResponse usuario : usuarios) {
                    mostrarUsuario(usuario);
                }
            }

            @Override
            public void onFailure(
                    Call<List<UsuarioResponse>> call,
                    Throwable t) {

                mostrarMensaje("No se pudo conectar con el servidor");
            }
        });
    }

    private void mostrarUsuario(UsuarioResponse usuario) {

        TextView info = new TextView(this);

        info.setText(
                "ID: " + usuario.getId() +
                "\nNombre: " + usuario.getNombre() +
                "\nCorreo: " + usuario.getCorreo() +
                "\nRol: " + usuario.getRol()
        );

        info.setTextSize(16);
        info.setPadding(0, 20, 0, 10);

        contenedorResultados.addView(info);

        LinearLayout botones = new LinearLayout(this);
        botones.setOrientation(LinearLayout.HORIZONTAL);

        Button btnEstudiante = new Button(this);
        btnEstudiante.setText("ESTUDIANTE");

        Button btnDocente = new Button(this);
        btnDocente.setText("DOCENTE");

        Button btnAdmin = new Button(this);
        btnAdmin.setText("ADMIN");

        btnEstudiante.setOnClickListener(v ->
                cambiarRol(usuario.getId(), "ESTUDIANTE"));

        btnDocente.setOnClickListener(v ->
                cambiarRol(usuario.getId(), "DOCENTE"));

        btnAdmin.setOnClickListener(v ->
                cambiarRol(usuario.getId(), "ADMIN"));

        botones.addView(btnEstudiante);
        botones.addView(btnDocente);
        botones.addView(btnAdmin);

        contenedorResultados.addView(botones);
    }

    private void cambiarRol(Long id, String rol) {

        ApiService apiService = RetrofitClient.getApiService();

        CambiarRolRequest request = new CambiarRolRequest(rol);

        apiService.cambiarRol(id, request).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(
                            BuscarUsuarioActivity.this,
                            "Rol cambiado a " + rol,
                            Toast.LENGTH_SHORT
                    ).show();

                    buscarUsuarios();

                } else {

                    Toast.makeText(
                            BuscarUsuarioActivity.this,
                            "Error al cambiar rol: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<ResponseBody> call,
                    Throwable t) {

                Toast.makeText(
                        BuscarUsuarioActivity.this,
                        "No se pudo conectar con el servidor",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void mostrarMensaje(String mensaje) {

        TextView texto = new TextView(this);
        texto.setText(mensaje);
        texto.setTextSize(16);

        contenedorResultados.addView(texto);
    }
}
