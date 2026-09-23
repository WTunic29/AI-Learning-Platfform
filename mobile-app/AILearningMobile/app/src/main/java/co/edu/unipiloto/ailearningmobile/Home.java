package co.edu.unipiloto.ailearningmobile;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        TextView textUsuario = findViewById(R.id.textUsuario);
        Button btnBuscarUsuario = findViewById(R.id.btnBuscarUsuario);
        Button btnCursos = findViewById(R.id.btnCursos);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Obtener el nombre enviado desde MainActivity
        String nombre = getIntent().getStringExtra("nombre");
        Long usuarioId = getIntent().getLongExtra("usuarioId", -1L);

        if (nombre != null && !nombre.isEmpty()) {

            textUsuario.setText("Bienvenido, " + nombre);

        }

        // Buscar usuarios
        btnBuscarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Home.this,
                    BuscarUsuarioActivity.class
            );
            startActivity(intent);
        });

        // catálogo cursos
        btnCursos.setOnClickListener(v -> {

            Intent intent = new Intent(Home.this, CursosActivity.class);
            intent.putExtra("usuarioId", usuarioId);

            startActivity(intent);

        });

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {

            Intent intent = new Intent(

                    Home.this,
                    MainActivity.class

                    );

            intent.setFlags(

                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK

            );

            startActivity(intent);
        });

    }

}
