package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.RegisterRequest;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder){

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Usuario registrar(RegisterRequest request){

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {

            throw new RuntimeException("El correo ya está registrado");

        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());

        //Transforma una contraseña en un BCrypt... La contraseña original no se almacena
        String passwordHash = passwordEncoder.encode(request.getPassword());

        usuario.setPasswordHash(passwordHash);
        usuario.setRol("ESTUDIANTE");

        return usuarioRepository.save(usuario);

    }

}
