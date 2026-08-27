package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.LoginRequest;
import co.edu.unipiloto.ailearning.api.dto.LoginResponse;
import co.edu.unipiloto.ailearning.api.dto.RegisterRequest;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterRequest request) {

        try {

            Usuario usuario = authService.registrar(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("El usuario ha sido registrado correctamente");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        try {

            Usuario usuario = authService.login(request);

            LoginResponse response = new LoginResponse(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getRol()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}
