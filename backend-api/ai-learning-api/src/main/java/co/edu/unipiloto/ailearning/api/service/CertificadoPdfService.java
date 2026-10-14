package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Certificado;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class CertificadoPdfService {

    public byte[] generarPdf(
            Certificado certificado) {

        PDRectangle landscape =
                new PDRectangle(
                        PDRectangle.A4.getHeight(),
                        PDRectangle.A4.getWidth()
                );

        try (
                PDDocument document =
                        new PDDocument();

                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {

            PDPage page =
                    new PDPage(landscape);

            document.addPage(page);

            float ancho =
                    page.getMediaBox()
                            .getWidth();

            float alto =
                    page.getMediaBox()
                            .getHeight();

            try (
                    PDPageContentStream content =
                            new PDPageContentStream(
                                    document,
                                    page
                            )
            ) {

                dibujarBordes(
                        content,
                        ancho,
                        alto
                );

                textoCentrado(
                        content,
                        "NEXALEARN",
                        PDType1Font.HELVETICA_BOLD,
                        30,
                        ancho,
                        alto - 95
                );

                textoCentrado(
                        content,
                        "CERTIFICADO DE APROBACION",
                        PDType1Font.HELVETICA_BOLD,
                        23,
                        ancho,
                        alto - 145
                );

                textoCentrado(
                        content,
                        "NexaLearn certifica que",
                        PDType1Font.HELVETICA,
                        14,
                        ancho,
                        alto - 205
                );

                textoCentrado(
                        content,
                        limpiar(
                                certificado
                                        .getUsuario()
                                        .getNombre()
                        ),
                        PDType1Font.HELVETICA_BOLD,
                        28,
                        ancho,
                        alto - 250
                );

                textoCentrado(
                        content,
                        "ha completado y aprobado satisfactoriamente el curso",
                        PDType1Font.HELVETICA,
                        14,
                        ancho,
                        alto - 300
                );

                textoCentrado(
                        content,
                        limpiar(
                                certificado
                                        .getCurso()
                                        .getNombre()
                        ),
                        PDType1Font.HELVETICA_BOLD,
                        21,
                        ancho,
                        alto - 345
                );

                textoCentrado(
                        content,
                        "Promedio final: "
                                + certificado
                                .getPromedio(),
                        PDType1Font.HELVETICA_BOLD,
                        16,
                        ancho,
                        alto - 405
                );

                textoCentrado(
                        content,
                        "Fecha de emision: "
                                + certificado
                                .getFechaEmision()
                                .format(
                                        DateTimeFormatter
                                                .ofPattern(
                                                        "dd/MM/yyyy"
                                                )
                                ),
                        PDType1Font.HELVETICA,
                        12,
                        ancho,
                        alto - 445
                );

                textoCentrado(
                        content,
                        "Codigo unico de verificacion",
                        PDType1Font.HELVETICA_BOLD,
                        11,
                        ancho,
                        alto - 495
                );

                textoCentrado(
                        content,
                        certificado
                                .getCodigoVerificacion(),
                        PDType1Font.COURIER_BOLD,
                        16,
                        ancho,
                        alto - 522
                );

                textoCentrado(
                        content,
                        "Documento generado digitalmente por NexaLearn",
                        PDType1Font.HELVETICA_OBLIQUE,
                        10,
                        ancho,
                        55
                );
            }

            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {

            throw new RuntimeException(
                    "No fue posible generar el certificado PDF",
                    e
            );
        }
    }

    private void dibujarBordes(
            PDPageContentStream content,
            float ancho,
            float alto) throws IOException {

        content.setLineWidth(3f);

        content.addRect(
                28,
                28,
                ancho - 56,
                alto - 56
        );

        content.stroke();

        content.setLineWidth(1f);

        content.addRect(
                40,
                40,
                ancho - 80,
                alto - 80
        );

        content.stroke();
    }

    private void textoCentrado(
            PDPageContentStream content,
            String texto,
            PDType1Font fuente,
            float tamano,
            float anchoPagina,
            float y) throws IOException {

        String limpio =
                limpiar(texto);

        float anchoTexto =
                fuente.getStringWidth(limpio)
                        / 1000f
                        * tamano;

        float x =
                (anchoPagina - anchoTexto)
                        / 2f;

        content.beginText();

        content.setFont(
                fuente,
                tamano
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(limpio);

        content.endText();
    }

    private String limpiar(
            String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("ñ", "n")
                .replace("Ñ", "N");
    }
}