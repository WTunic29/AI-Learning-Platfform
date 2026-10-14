package co.edu.unipiloto.ailearning.api.controller;

import co.edu.unipiloto.ailearning.api.dto.CertificadoResponse;
import co.edu.unipiloto.ailearning.api.service.CertificadoService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;

    public CertificadoController(
            CertificadoService certificadoService) {

        this.certificadoService = certificadoService;
    }

    @PostMapping(
            "/usuario/{usuarioId}/curso/{cursoId}"
    )
    public ResponseEntity<CertificadoResponse>
    generarCertificado(
            @PathVariable Long usuarioId,
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(
                certificadoService.generar(
                        usuarioId,
                        cursoId
                )
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CertificadoResponse>>
    obtenerCertificados(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                certificadoService
                        .obtenerPorUsuario(usuarioId)
        );
    }

    @GetMapping(
            value = "/{certificadoId}/pdf",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long certificadoId,
            @RequestParam Long usuarioId) {

        byte[] pdf =
                certificadoService
                        .descargarPdf(
                                certificadoId,
                                usuarioId
                        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificado-nexalearn.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}