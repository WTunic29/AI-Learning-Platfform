package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.LoginRequest;
import co.edu.unipiloto.ailearning.api.dto.RegisterRequest;
import co.edu.unipiloto.ailearning.api.dto.RestablecerPasswordRequest;
import co.edu.unipiloto.ailearning.api.dto.SolicitarRecuperacionResponse;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;
import co.edu.unipiloto.ailearning.api.model.Rol;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom random =
            new SecureRandom();

    private final Map<String, CodigoRecuperacion>
            codigosRecuperacion =
            new ConcurrentHashMap<>();

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(RegisterRequest request) {

        if (usuarioRepository
                .existsByCorreo(request.getCorreo())) {

            throw new RuntimeException(
                    "El correo ya esta registrado"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(
                request.getNombre()
        );

        usuario.setCorreo(
                request.getCorreo()
        );

        usuario.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        usuario.setRol(
                Rol.ESTUDIANTE
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario login(LoginRequest request) {

        Usuario usuario =
                usuarioRepository
                        .findByCorreo(
                                request.getCorreo()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Credenciales invalidas"
                                )
                        );

        if (!passwordEncoder.matches(
                request.getPassword(),
                usuario.getPasswordHash())) {

            throw new RuntimeException(
                    "Credenciales invalidas"
            );
        }

        return usuario;
    }

    public SolicitarRecuperacionResponse
    solicitarRecuperacion(String correo) {

        if (correo == null
                || correo.trim().isEmpty()) {

            throw new RuntimeException(
                    "El correo es obligatorio"
            );
        }

        String correoNormalizado =
                correo.trim().toLowerCase();

        String codigo =
                String.format(
                        "%06d",
                        random.nextInt(1000000)
                );

        var usuario =
                usuarioRepository
                        .findByCorreoIgnoreCase(
                                correoNormalizado
                        );

        if (usuario.isPresent()) {

            codigosRecuperacion.put(
                    correoNormalizado,
                    new CodigoRecuperacion(
                            codigo,
                            LocalDateTime
                                    .now()
                                    .plusMinutes(10)
                    )
            );
        }

        return new SolicitarRecuperacionResponse(
                "Si el correo existe, se genero un codigo de recuperacion.",
                codigo
        );
    }

    public void restablecerPassword(
            RestablecerPasswordRequest request) {

        if (request.getCorreo() == null
                || request.getCodigo() == null
                || request.getNuevaPassword() == null) {

            throw new RuntimeException(
                    "Todos los campos son obligatorios"
            );
        }

        if (request
                .getNuevaPassword()
                .length() < 6) {

            throw new RuntimeException(
                    "La nueva password debe tener al menos 6 caracteres"
            );
        }

        String correo =
                request.getCorreo()
                        .trim()
                        .toLowerCase();

        CodigoRecuperacion recuperacion =
                codigosRecuperacion
                        .get(correo);

        if (recuperacion == null) {

            throw new RuntimeException(
                    "Codigo invalido o vencido"
            );
        }

        if (LocalDateTime.now()
                .isAfter(recuperacion.expira())) {

            codigosRecuperacion.remove(
                    correo
            );

            throw new RuntimeException(
                    "Codigo invalido o vencido"
            );
        }

        if (!recuperacion.codigo()
                .equals(
                        request.getCodigo().trim()
                )) {

            throw new RuntimeException(
                    "Codigo invalido o vencido"
            );
        }

        Usuario usuario =
                usuarioRepository
                        .findByCorreoIgnoreCase(
                                correo
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Codigo invalido o vencido"
                                )
                        );

        usuario.setPasswordHash(
                passwordEncoder.encode(
                        request.getNuevaPassword()
                )
        );

        usuarioRepository.save(usuario);

        codigosRecuperacion.remove(
                correo
        );
    }

    private record CodigoRecuperacion(
            String codigo,
            LocalDateTime expira) {
    }
}