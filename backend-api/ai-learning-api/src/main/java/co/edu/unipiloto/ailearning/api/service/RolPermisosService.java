package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Permiso;
import co.edu.unipiloto.ailearning.api.model.Rol;

import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class RolPermisosService {

    public Set<Permiso> obtenerPermisosPorRol(Rol rol) {

        switch (rol) {

            case ESTUDIANTE:
                return EnumSet.of(
                        Permiso.VER_CURSOS,
                        Permiso.BUSCAR_CURSOS,
                        Permiso.INSCRIBIRSE_CURSO,
                        Permiso.VER_CONTENIDOS
                );

            case DOCENTE:
                return EnumSet.of(
                        Permiso.CREAR_ACTIVIDAD,
                        Permiso.REGISTRAR_CALIFICACION
                );

            case ADMIN:
                return EnumSet.of(
                        Permiso.BUSCAR_USUARIOS
                );

            case SUPERADMIN:
                return EnumSet.allOf(Permiso.class);

            default:
                return EnumSet.noneOf(Permiso.class);
        }
    }
}