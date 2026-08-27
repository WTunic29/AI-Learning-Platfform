package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.ailearningmobile.dto.RegisterRequest;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etCorreo;
    private EditText etPassword;

    private Button btnRegistrar;

    private TextView tvVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvVolverLogin = findViewById(R.id.tvVolverLogin);

        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        tvVolverLogin.setOnClickListener(v -> finish());
    }

    private void registrarUsuario() {

        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (password.length() < 8) {

            Toast.makeText(
                    this,
                    "La contraseña debe tener mínimo 8 caracteres",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        RegisterRequest request =
                new RegisterRequest(nombre, correo, password);

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService.register(request).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response
            ) {

                if (response.isSuccessful()) {

                    Toast.makeText(
                            RegisterActivity.this,
                            "Cuenta creada correctamente",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();

                } else if (response.code() == 409) {

                    Toast.makeText(
                            RegisterActivity.this,
                            "El correo ya está registrado",
                            Toast.LENGTH_LONG
                    ).show();

                } else {

                    Toast.makeText(
                            RegisterActivity.this,
                            "No fue posible crear la cuenta",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<ResponseBody> call,
                    Throwable t
            ) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}