package co.edu.unipiloto.ailearningmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textUsuario = findViewById(R.id.textUsuario);

        Button btnBuscarUsuario = findViewById(R.id.btnBuscarUsuario);
        Button btnCursos = findViewById(R.id.btnCursos);
        Button btnGestionarActividades = findViewById(R.id.btnGestionarActividades);
        Button btnCalificarActividades = findViewById(R.id.btnCalificarActividades);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        String nombre = getIntent().getStringExtra("nombre");
        long usuarioId = getIntent().getLongExtra("usuarioId", -1L);
        String rol = getIntent().getStringExtra("rol");

        android.util.Log.d("HOME", "Rol recibido: " + rol);
        android.util.Log.d("HOME", "Usuario ID: " + usuarioId);

        if (nombre != null && !nombre.isEmpty()) {
            textUsuario.setText("Bienvenido, " + nombre);
        }

        /*
         * Funciones exclusivas del DOCENTE
         */
        if ("DOCENTE".equalsIgnoreCase(rol)) {

            btnGestionarActividades.setVisibility(View.VISIBLE);
            btnCalificarActividades.setVisibility(View.VISIBLE);

        } else {

            btnGestionarActividades.setVisibility(View.GONE);
            btnCalificarActividades.setVisibility(View.GONE);
        }

        btnGestionarActividades.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CrearActividadActivity.class
                    );

            intent.putExtra("usuarioId", usuarioId);

            startActivity(intent);
        });

        btnCalificarActividades.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CalificarActividadActivity.class
                    );

            intent.putExtra("usuarioId", usuarioId);

            startActivity(intent);
        });

        btnBuscarUsuario.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            BuscarUsuarioActivity.class
                    );

            intent.putExtra("usuarioId", usuarioId);

            startActivity(intent);
        });

        btnCursos.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CursosActivity.class
                    );

            intent.putExtra("usuarioId", usuarioId);

            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });
    }
}