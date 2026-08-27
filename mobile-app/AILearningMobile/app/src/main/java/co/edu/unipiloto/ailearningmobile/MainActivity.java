package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.unipiloto.ailearningmobile.dto.LoginRequest;
import co.edu.unipiloto.ailearningmobile.dto.LoginResponse;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // probarConexionDirecta(); Test sockets

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()

                    );

                    v.setPadding(systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom

                    );

                    return insets;
                }

        );

        // Referencias a los elementos de la pantalla
        EditText editCorreo = findViewById(R.id.editCorreo);
        EditText editPassword = findViewById(R.id.editPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvCrearCuenta = findViewById(R.id.tvCrearCuenta);

        tvCrearCuenta.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });

        // Acción del botón Iniciar sesión
        btnLogin.setOnClickListener(v -> {

            String correo = editCorreo.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            /*
            // Validación básica
            if (correo.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Complete todos los campos",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
            */

            // Crear la petición
            LoginRequest request = new LoginRequest(correo, password);

            ApiService apiService = RetrofitClient.getApiService();// Obtener el servicio Retrofit
            Call<LoginResponse> call = apiService.login(request);// Le dice a Retrofit que ejecute la petición de manera asíncrona
            // para que la app no se congele
            call.enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        LoginResponse loginResponse = response.body();

                        Log.d(

                                "LOGIN",
                                "Login exitoso: " + loginResponse.getNombre()

                        );

                        Toast.makeText(MainActivity.this,
                                "Bienvenido" + loginResponse.getNombre(),
                                Toast.LENGTH_SHORT

                        ).show();

                        //aqui dice quiero ir de MainActivity a Home
                        Intent intent = new Intent(
                                MainActivity.this,
                                Home.class
                        );

                       intent.putExtra(
                               "nombre",
                               loginResponse.getNombre()
                        );

                       startActivity(intent);
                       finish();

                    } else {

                        Log.e(
                                "LOGIN",
                                "Error HTTP: " + response.code()
                        );

                        Toast.makeText(

                                MainActivity.this,
                                "Correo o contraseña invalidos: " + response.code(),
                                Toast.LENGTH_SHORT

                        ).show();

                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Log.e("LOGIN", "Error de conexión", t);

                    Toast.makeText(MainActivity.this,
                            "No se pudo conectar con el servidor",
                            Toast.LENGTH_SHORT

                    ).show();

                }

            });

        });

    }

    // PRUEBA SOCKETS
    /*
    private void probarConexionDirecta() {

        new Thread(() -> {

            try {

                java.net.Socket socket = new java.net.Socket();

                socket.connect(
                        new java.net.InetSocketAddress(
                                "127.0.0.1",
                                8080
                        ),
                        5000
                );

                Log.d(
                        "TEST_SOCKET",
                        "CONEXION TCP EXITOSA"
                );

                socket.close();

            } catch (Exception e) {

                Log.e(
                        "TEST_SOCKET",
                        "CONEXION TCP FALLIDA",
                        e
                );
            }

        }).start();
    }
    */
}