package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.dto.CertificadoResponse;
import co.edu.unipiloto.ailearning.api.dto.ProgresoCursoResponse;
import co.edu.unipiloto.ailearning.api.exception.AccesoDenegadoException;
import co.edu.unipiloto.ailearning.api.model.*;
import co.edu.unipiloto.ailearning.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
public class CertificadoService {

    private static final BigDecimal NOTA_MINIMA =
            new BigDecimal("3.00");

    private final CertificadoRepository certificadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ActivityRepository actividadRepository;
    private final CalificacionRepository calificacionRepository;
    private final ProgresoService progresoService;
    private final CertificadoPdfService certificadoPdfService;

    public CertificadoService(
            CertificadoRepository certificadoRepository,
            UsuarioRepository usuarioRepository,
            CursoRepository cursoRepository,
            InscripcionRepository inscripcionRepository,
            ActivityRepository actividadRepository,
            CalificacionRepository calificacionRepository,
            ProgresoService progresoService,
            CertificadoPdfService certificadoPdfService) {

        this.certificadoRepository = certificadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.actividadRepository = actividadRepository;
        this.calificacionRepository = calificacionRepository;
        this.progresoService = progresoService;
        this.certificadoPdfService = certificadoPdfService;
    }

    @Transactional
    public CertificadoResponse generar(
            Long usuarioId,
            Long cursoId) {

        Usuario usuario =
                usuarioRepository.findById(usuarioId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El usuario no existe"
                                )
                        );

        if (usuario.getRol() != Rol.ESTUDIANTE) {
            throw new AccesoDenegadoException(
                    "Solo los estudiantes pueden generar certificados"
            );
        }

        Curso curso =
                cursoRepository.findById(cursoId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El curso no existe"
                                )
                        );

        boolean inscrito =
                inscripcionRepository
                        .existsByUsuarioIdAndCursoId(
                                usuarioId,
                                cursoId
                        );

        if (!inscrito) {
            throw new AccesoDenegadoException(
                    "El estudiante no esta inscrito en este curso"
            );
        }

        var existente =
                certificadoRepository
                        .findByUsuarioIdAndCursoId(
                                usuarioId,
                                cursoId
                        );

        if (existente.isPresent()) {
            return convertir(existente.get());
        }

        ProgresoCursoResponse progreso =
                progresoService.obtenerProgresoCurso(
                        usuarioId,
                        cursoId
                );

        if (progreso.getPorcentajeAvance() < 100.0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El curso aun no esta finalizado. Debes completar el 100% de los modulos."
            );
        }

        List<Actividad> actividades =
                actividadRepository.findByCursoId(cursoId);

        if (actividades.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El curso no tiene actividades evaluables."
            );
        }

        BigDecimal sumaPonderada = BigDecimal.ZERO;
        BigDecimal sumaPesos = BigDecimal.ZERO;

        for (Actividad actividad : actividades) {

            Calificacion calificacion =
                    calificacionRepository
                            .findByActividadIdAndEstudianteId(
                                    actividad.getId(),
                                    usuarioId
                            )
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.CONFLICT,
                                            "Existen actividades pendientes por calificar."
                                    )
                            );

            BigDecimal peso = actividad.getPonderacion();

            if (peso == null ||
                    peso.compareTo(BigDecimal.ZERO) <= 0) {
                peso = BigDecimal.ONE;
            }

            sumaPonderada =
                    sumaPonderada.add(
                            calificacion.getNota()
                                    .multiply(peso)
                    );

            sumaPesos =
                    sumaPesos.add(peso);
        }

        BigDecimal promedio =
                sumaPonderada.divide(
                        sumaPesos,
                        2,
                        RoundingMode.HALF_UP
                );

        if (promedio.compareTo(NOTA_MINIMA) < 0) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El curso esta finalizado pero no fue aprobado. Promedio: "
                            + promedio
            );
        }

        Certificado certificado = new Certificado();

        certificado.setUsuario(usuario);
        certificado.setCurso(curso);
        certificado.setPromedio(promedio);

        certificado.setCodigoVerificacion(
                generarCodigo()
        );

        return convertir(
                certificadoRepository.save(certificado)
        );
    }

    @Transactional(readOnly = true)
    public List<CertificadoResponse> obtenerPorUsuario(
            Long usuarioId) {

        return certificadoRepository
                .findByUsuarioIdOrderByFechaEmisionDesc(usuarioId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public byte[] descargarPdf(
            Long certificadoId,
            Long usuarioId) {

        Certificado certificado =
                certificadoRepository
                        .findById(certificadoId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El certificado no existe"
                                )
                        );

        if (certificado.getUsuario().getId() != usuarioId) {

            throw new AccesoDenegadoException(
                    "No tienes acceso a este certificado"
            );
        }

        return certificadoPdfService
                .generarPdf(certificado);
    }

    private String generarCodigo() {

        return "NXL-" +
                Year.now().getValue() +
                "-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private CertificadoResponse convertir(
            Certificado certificado) {

        return new CertificadoResponse(
                certificado.getId(),
                certificado.getUsuario().getId(),
                certificado.getUsuario().getNombre(),
                certificado.getCurso().getId(),
                certificado.getCurso().getNombre(),
                certificado.getCodigoVerificacion(),
                certificado.getPromedio(),
                certificado.getFechaEmision()
        );
    }
}