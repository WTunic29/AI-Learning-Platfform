package co.edu.unipiloto.ailearningmobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.ailearningmobile.dto.*;
import co.edu.unipiloto.ailearningmobile.network.ApiService;
import co.edu.unipiloto.ailearningmobile.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarPasswordActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recuperar_password
        );

        EditText correo =
                findViewById(
                        R.id.editCorreoRecuperacion
                );

        EditText codigo =
                findViewById(
                        R.id.editCodigoRecuperacion
                );

        EditText nuevaPassword =
                findViewById(
                        R.id.editNuevaPassword
                );

        TextView codigoDemo =
                findViewById(
                        R.id.tvCodigoDemo
                );

        Button solicitar =
                findViewById(
                        R.id.btnSolicitarCodigo
                );

        Button restablecer =
                findViewById(
                        R.id.btnRestablecerPassword
                );

        ApiService api =
                RetrofitClient.getApiService();

        solicitar.setOnClickListener(v -> {

            String email =
                    correo.getText()
                            .toString()
                            .trim();

            if (email.isEmpty()) {

                Toast.makeText(
                        this,
                        "Ingresa tu correo",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            api.solicitarRecuperacion(
                    new SolicitarRecuperacionRequest(
                            email
                    )
            ).enqueue(
                    new Callback<SolicitarRecuperacionResponse>() {

                @Override
                public void onResponse(
                        Call<SolicitarRecuperacionResponse> call,
                        Response<SolicitarRecuperacionResponse> response) {

                    if (response.isSuccessful()
                            && response.body() != null) {

                        String codigoRecibido =
                                response.body()
                                        .getCodigoDemo();

                        codigoDemo.setText(
                                "Código temporal (modo demo): "
                                        + codigoRecibido
                        );

                        codigo.setText(
                                codigoRecibido
                        );

                        Toast.makeText(
                                RecuperarPasswordActivity.this,
                                "Código generado. Válido durante 10 minutos.",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                RecuperarPasswordActivity.this,
                                "No fue posible generar el código.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<SolicitarRecuperacionResponse> call,
                        Throwable t) {

                    Toast.makeText(
                            RecuperarPasswordActivity.this,
                            "Error de conexión.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });

        restablecer.setOnClickListener(v -> {

            String email =
                    correo.getText()
                            .toString()
                            .trim();

            String codigoIngresado =
                    codigo.getText()
                            .toString()
                            .trim();

            String password =
                    nuevaPassword.getText()
                            .toString();

            if (email.isEmpty()
                    || codigoIngresado.isEmpty()
                    || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Completa todos los campos.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            api.restablecerPassword(
                    new RestablecerPasswordRequest(
                            email,
                            codigoIngresado,
                            password
                    )
            ).enqueue(
                    new Callback<ResponseBody>() {

                @Override
                public void onResponse(
                        Call<ResponseBody> call,
                        Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(
                                RecuperarPasswordActivity.this,
                                "Contraseña actualizada correctamente.",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                RecuperarPasswordActivity.this,
                                "Código inválido, vencido o contraseña no válida.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<ResponseBody> call,
                        Throwable t) {

                    Toast.makeText(
                            RecuperarPasswordActivity.this,
                            "Error de conexión.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
    }
}