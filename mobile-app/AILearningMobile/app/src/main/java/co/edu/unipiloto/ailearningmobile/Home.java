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

        setContentView(
                R.layout.activity_home
        );

        /*
         * Barra superior acorde al diseño.
         */
        getWindow().setStatusBarColor(
                android.graphics.Color.rgb(
                        9,
                        11,
                        24
                )
        );

        TextView textUsuario =
                findViewById(
                        R.id.textUsuario
                );

        Button btnBuscarUsuario =
                findViewById(
                        R.id.btnBuscarUsuario
                );

        Button btnCursos =
                findViewById(
                        R.id.btnCursos
                );

        Button btnMisCursos =
                findViewById(
                        R.id.btnMisCursos
                );

        Button btnGestionarActividades =
                findViewById(
                        R.id.btnGestionarActividades
                );

        Button btnCalificarActividades =
                findViewById(
                        R.id.btnCalificarActividades
                );

        Button btnCertificados =
                findViewById(
                        R.id.btnCertificados
                );

        Button btnCentroAcademico =
                findViewById(
                        R.id.btnCentroAcademico
                );

        Button btnEspacios =
                findViewById(
                        R.id.btnEspacios
                );

        Button btnCerrarSesion =
                findViewById(
                        R.id.btnCerrarSesion
                );

        String nombre =
                getIntent()
                        .getStringExtra(
                                "nombre"
                        );

        long usuarioId =
                getIntent()
                        .getLongExtra(
                                "usuarioId",
                                -1L
                        );

        String rol =
                getIntent()
                        .getStringExtra(
                                "rol"
                        );

        if (
                nombre != null
                        &&
                !nombre.isEmpty()
        ) {

            textUsuario.setText(
                    nombre
                            + " • "
                            + (
                            rol != null
                                    ? rol
                                    : "Usuario"
                    )
            );
        }

        /*
         * Todas las opciones específicas
         * empiezan ocultas.
         */
        btnBuscarUsuario
                .setVisibility(
                        View.GONE
                );

        btnMisCursos
                .setVisibility(
                        View.GONE
                );

        btnGestionarActividades
                .setVisibility(
                        View.GONE
                );

        btnCalificarActividades
                .setVisibility(
                        View.GONE
                );

        btnCertificados
                .setVisibility(
                        View.GONE
                );

        /*
         * ESTUDIANTE
         */
        if (
                "ESTUDIANTE"
                        .equalsIgnoreCase(
                                rol
                        )
        ) {

            btnMisCursos
                    .setVisibility(
                            View.VISIBLE
                    );

            btnCertificados
                    .setVisibility(
                            View.VISIBLE
                    );
        }

        /*
         * DOCENTE
         */
        else if (
                "DOCENTE"
                        .equalsIgnoreCase(
                                rol
                        )
        ) {

            btnGestionarActividades
                    .setVisibility(
                            View.VISIBLE
                    );

            btnCalificarActividades
                    .setVisibility(
                            View.VISIBLE
                    );
        }

        /*
         * ADMIN
         */
        else if (
                "ADMIN"
                        .equalsIgnoreCase(
                                rol
                        )
                        ||
                "SUPERADMIN"
                        .equalsIgnoreCase(
                                rol
                        )
        ) {

            btnBuscarUsuario
                    .setVisibility(
                            View.VISIBLE
                    );
        }

        /*
         * CATÁLOGO
         */
        btnCursos
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CursosActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            startActivity(
                    intent
            );
        });

        /*
         * MIS CURSOS
         */
        btnMisCursos
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            MisCursosActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            startActivity(
                    intent
            );
        });

        /*
         * ADMIN USUARIOS
         */
        btnBuscarUsuario
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            BuscarUsuarioActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            startActivity(
                    intent
            );
        });

        /*
         * ACTIVIDADES
         */
        btnGestionarActividades
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CrearActividadActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            startActivity(
                    intent
            );
        });

        /*
         * CALIFICACIONES
         */
        btnCalificarActividades
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CalificarActividadActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            startActivity(
                    intent
            );
        });

        /*
         * CERTIFICADOS
         */
        btnCertificados
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CertificadoActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            intent.putExtra(
                    "cursoId",
                    1L
            );

            startActivity(
                    intent
            );
        });

        /*
         * CENTRO ACADÉMICO
         */
        btnCentroAcademico
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            CentroAcademicoActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            intent.putExtra(
                    "rol",
                    rol
            );

            startActivity(
                    intent
            );
        });

        /*
         * GEOLOCALIZACIÓN /
         * ESPACIOS EDUCATIVOS
         */
        btnEspacios
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            MapaEspaciosEducativosActivity.class
                    );

            intent.putExtra(
                    "usuarioId",
                    usuarioId
            );

            startActivity(
                    intent
            );
        });

        /*
         * CERRAR SESIÓN
         */
        btnCerrarSesion
                .setOnClickListener(
                        v -> {

            Intent intent =
                    new Intent(
                            Home.this,
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(
                    intent
            );
        });

        /*
         * Animación de entrada.
         */
        View contenido =
                findViewById(
                        R.id.main
                );

        contenido.setAlpha(
                0f
        );

        contenido.setTranslationY(
                40f
        );

        contenido.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(650)
                .start();
    }
}