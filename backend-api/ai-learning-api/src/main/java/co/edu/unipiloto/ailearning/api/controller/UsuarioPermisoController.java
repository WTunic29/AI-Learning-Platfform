package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.model.Permiso;
import co.edu.unipiloto.ailearning.api.model.UsuarioPermiso;
import co.edu.unipiloto.ailearning.api.service.UsuarioPermisoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPermisoController {

    private final UsuarioPermisoService usuarioPermisoService;

    public UsuarioPermisoController(UsuarioPermisoService usuarioPermisoService) {
        this.usuarioPermisoService = usuarioPermisoService;
    }

    @PostMapping("/{usuarioId}/permisos/{permiso}")
    public ResponseEntity<UsuarioPermiso> concederPermiso(
            @PathVariable Long usuarioId, @PathVariable Permiso permiso, @RequestParam Long actorId) {

        return ResponseEntity.ok(usuarioPermisoService.concederPermiso(usuarioId, permiso, actorId));
    }

    @GetMapping("/{usuarioId}/permisos")
    public ResponseEntity<List<UsuarioPermiso>> obtenerPermisos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioPermisoService.obtenerPermisos(usuarioId));
    }

    @GetMapping("/{usuarioId}/permisos/efectivos")
    public ResponseEntity<Set<Permiso>> obtenerPermisosEfectivos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioPermisoService.obtenerPermisosEfectivos(usuarioId));
    }

    @DeleteMapping("/{usuarioId}/permisos/{permiso}")
    public ResponseEntity<Void> revocarPermiso(@PathVariable Long usuarioId, @PathVariable Permiso permiso, @RequestParam Long actorId) {

        usuarioPermisoService.revocarPermiso(usuarioId, permiso, actorId);
        return ResponseEntity.noContent().build();

    }

}
