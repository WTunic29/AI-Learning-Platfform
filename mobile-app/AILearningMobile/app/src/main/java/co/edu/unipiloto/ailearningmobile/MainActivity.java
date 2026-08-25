package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()                    );

                    v.setPadding(
                            systemBars.left,
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

        // Acción del botón Iniciar sesión
        btnLogin.setOnClickListener(v -> {

            String correo = editCorreo.getText().toString().trim();
            String password = editPassword.getText().toString();

            // Validación básica
            if (correo.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Complete todos los campos",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Crear la petición
            LoginRequest request = new LoginRequest(
                    correo,
                    password
            );

            ApiService apiService = RetrofitClient.getApiService(); // Obtener el servicio Retrofit
            Call<LoginResponse> call = apiService.login(request); // Crear la llamada
            call.enqueue(new Callback<LoginResponse>() { //Le dice a retrofit que ejecute la peticion de manera asincrona... para que la app no se congele

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.isSuccessful()) {

                        LoginResponse loginResponse = response.body();

                        Log.d(
                                "LOGIN",
                                "Login exitoso: "
                                        + loginResponse.getNombre()
                        );

                    } else {

                        Log.e(
                                "LOGIN",
                                "Error HTTP: " + response.code()
                        );

                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Log.e(
                            "LOGIN",
                            "Error de conexión",
                            t
                    );

                }

            });

        });

    }

}