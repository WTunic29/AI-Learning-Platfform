package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.CambiaRolRequest;
import co.edu.unipiloto.ailearning.api.model.Usuario;
import co.edu.unipiloto.ailearning.api.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PutMapping("{id}/rol")
    public ResponseEntity<Usuario> cambiarRol(@PathVariable Long id,
            @Valid @RequestBody CambiaRolRequest request){

        Usuario usuario = usuarioService.cambiarRol(id, request.getRol());

        return ResponseEntity.ok(usuario);
    }

}
