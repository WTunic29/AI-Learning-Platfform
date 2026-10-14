package co.edu.unipiloto.ailearning.api.service;

import co.edu.unipiloto.ailearning.api.model.Certificado;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class CertificadoPdfService {

    public byte[] generarPdf(Certificado certificado) {

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                escribir(
                        content,
                        "NEXALEARN",
                        PDType1Font.HELVETICA_BOLD,
                        26,
                        230,
                        700
                );

                escribir(
                        content,
                        "CERTIFICADO DE APROBACION",
                        PDType1Font.HELVETICA_BOLD,
                        20,
                        145,
                        650
                );

                escribir(
                        content,
                        "NexaLearn certifica que:",
                        PDType1Font.HELVETICA,
                        13,
                        220,
                        590
                );

                escribir(
                        content,
                        certificado.getUsuario().getNombre(),
                        PDType1Font.HELVETICA_BOLD,
                        22,
                        180,
                        545
                );

                escribir(
                        content,
                        "ha completado y aprobado satisfactoriamente el curso:",
                        PDType1Font.HELVETICA,
                        13,
                        120,
                        500
                );

                escribir(
                        content,
                        certificado.getCurso().getNombre(),
                        PDType1Font.HELVETICA_BOLD,
                        17,
                        120,
                        455
                );

                escribir(
                        content,
                        "Promedio final: " + certificado.getPromedio(),
                        PDType1Font.HELVETICA,
                        13,
                        210,
                        405
                );

                escribir(
                        content,
                        "Fecha de emision: " +
                                certificado.getFechaEmision()
                                        .format(
                                                DateTimeFormatter.ofPattern(
                                                        "dd/MM/yyyy HH:mm"
                                                )
                                        ),
                        PDType1Font.HELVETICA,
                        11,
                        190,
                        355
                );

                escribir(
                        content,
                        "Codigo de verificacion:",
                        PDType1Font.HELVETICA_BOLD,
                        11,
                        215,
                        305
                );

                escribir(
                        content,
                        certificado.getCodigoVerificacion(),
                        PDType1Font.COURIER_BOLD,
                        12,
                        170,
                        280
                );

                escribir(
                        content,
                        "NexaLearn - Plataforma de formacion en Inteligencia Artificial",
                        PDType1Font.HELVETICA_OBLIQUE,
                        9,
                        135,
                        120
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

    private void escribir(
            PDPageContentStream content,
            String texto,
            PDType1Font fuente,
            float tamano,
            float x,
            float y) throws IOException {

        content.beginText();
        content.setFont(fuente, tamano);
        content.newLineAtOffset(x, y);
        content.showText(limpiar(texto));
        content.endText();
    }

    private String limpiar(String texto) {

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