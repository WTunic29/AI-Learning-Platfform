package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.CambiaRolRequest;
import co.edu.unipiloto.ailearning.api.dto.UsuarioResponse;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponse>> buscarUsuarios(
            @RequestParam String termino) {

        return ResponseEntity.ok(
                usuarioService.buscarUsuarios(termino));
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiaRolRequest request,
            @RequestParam Long actorId) {

        return ResponseEntity.ok(
                usuarioService.cambiarRol(
                        id,
                        request.getRol(),
                        actorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam Long actorId) {

        usuarioService.eliminar(id, actorId);

        return ResponseEntity.noContent().build();
    }
}