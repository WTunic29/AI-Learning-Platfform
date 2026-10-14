package co.edu.unipiloto.ailearning.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, String>> manejarAccesoDenegado(AccesoDenegadoException ex){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Acceso denegado",
                        "Mensaje", ex.getMessage()));

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentoInvalido(IllegalArgumentException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Recurso no encontrado",
                "Mensaje", ex.getMessage()));

    }

}
