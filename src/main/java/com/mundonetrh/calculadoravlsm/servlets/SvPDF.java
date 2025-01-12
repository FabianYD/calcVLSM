package com.mundonetrh.calculadoravlsm.servlets;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Safelist;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet(name = "SvPDF", urlPatterns = {"/SvPDF"})
public class SvPDF extends HttpServlet {

    // Colores personalizados
    private static final BaseColor COLOR_TITULO = new BaseColor(33, 150, 243); // Azul
    private static final BaseColor COLOR_SECCION = new BaseColor(63, 81, 181); // Indigo
    private static final BaseColor COLOR_FONDO_TABLA = new BaseColor(237, 245, 255); // Azul claro
    private static final BaseColor COLOR_TEXTO = new BaseColor(33, 33, 33); // Casi negro
    private static final BaseColor COLOR_BORDE = new BaseColor(158, 158, 158); // Gris

    private String processText(String text) {
        String decoded = StringEscapeUtils.unescapeHtml4(text);
        return decoded.replace("<->", "↔")
                     .replace("=>", "⇒")
                     .replace("->", "→")
                     .replace("&lt;-&gt;", "↔")
                     .replace("==&gt;", "⇒")
                     .replace("--&gt;", "→");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            
            String redPrincipal = (String) session.getAttribute("red");
            String subredesHtml = (String) session.getAttribute("subredes");
            String resultadosHtml = (String) session.getAttribute("resultados");
            String hostsHtml = (String) session.getAttribute("hosts");
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=ReporteVLSM.pdf");
            response.setCharacterEncoding("UTF-8");
            
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            
            // Configurar fuentes
            BaseFont unicodeFont = BaseFont.createFont("C:/Windows/Fonts/msgothic.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font tituloFont = new Font(unicodeFont, 24, Font.BOLD, COLOR_TITULO);
            Font seccionFont = new Font(unicodeFont, 16, Font.BOLD, COLOR_SECCION);
            Font normalFont = new Font(unicodeFont, 12, Font.NORMAL, COLOR_TEXTO);
            Font monospaceFont = new Font(unicodeFont, 12, Font.NORMAL, COLOR_TEXTO);
            Font smallFont = new Font(unicodeFont, 10, Font.NORMAL, COLOR_TEXTO);
            
            // Agregar título principal con línea decorativa
            Paragraph title = new Paragraph("Reporte de Cálculo VLSM", tituloFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Línea decorativa
            PdfPTable lineaDecorativa = new PdfPTable(1);
            lineaDecorativa.setWidthPercentage(50);
            PdfPCell lineaCell = new PdfPCell();
            lineaCell.setBackgroundColor(COLOR_TITULO);
            lineaCell.setFixedHeight(3f);
            lineaCell.setBorder(Rectangle.NO_BORDER);
            lineaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            lineaDecorativa.addCell(lineaCell);
            document.add(lineaDecorativa);
            document.add(Chunk.NEWLINE);
            
            // Red Principal con fondo
            PdfPTable redTable = new PdfPTable(1);
            redTable.setWidthPercentage(100);
            PdfPCell redCell = new PdfPCell();
            redCell.setBackgroundColor(COLOR_FONDO_TABLA);
            redCell.setPadding(15);
            
            Paragraph redTitle = new Paragraph("Red Principal", seccionFont);
            redTitle.setSpacingAfter(10);
            redCell.addElement(redTitle);
            
            Paragraph redContent = new Paragraph(redPrincipal, normalFont);
            redCell.addElement(redContent);
            
            redTable.addCell(redCell);
            document.add(redTable);
            document.add(Chunk.NEWLINE);
            
            // Tabla de Hosts
            if (hostsHtml != null && !hostsHtml.isEmpty()) {
                Paragraph hostTitle = new Paragraph("Tabla de Hosts", seccionFont);
                hostTitle.setSpacingAfter(10);
                document.add(hostTitle);
                
                org.jsoup.nodes.Document doc = Jsoup.parse(hostsHtml);
                org.jsoup.select.Elements rows = doc.select("tr");
                
                if (!rows.isEmpty()) {
                    PdfPTable hostsTable = new PdfPTable(3);
                    hostsTable.setWidthPercentage(100);
                    hostsTable.setSpacingBefore(10);
                    
                    // Estilo para las celdas
                    for (org.jsoup.nodes.Element th : rows.first().select("th")) {
                        PdfPCell cell = new PdfPCell(new Phrase(processText(th.text()), normalFont));
                        cell.setBackgroundColor(COLOR_FONDO_TABLA);
                        cell.setBorderColor(COLOR_BORDE);
                        cell.setPadding(8);
                        hostsTable.addCell(cell);
                    }
                    
                    for (int i = 1; i < rows.size(); i++) {
                        org.jsoup.nodes.Element row = rows.get(i);
                        for (org.jsoup.nodes.Element td : row.select("td")) {
                            PdfPCell cell = new PdfPCell(new Phrase(processText(td.text()), smallFont));
                            cell.setBorderColor(COLOR_BORDE);
                            cell.setPadding(6);
                            hostsTable.addCell(cell);
                        }
                    }
                    
                    document.add(hostsTable);
                    document.add(Chunk.NEWLINE);
                }
            }
            
            // Tabla de Resultados
            if (resultadosHtml != null && !resultadosHtml.isEmpty()) {
                Paragraph resultTitle = new Paragraph("Tabla de Resultados", seccionFont);
                resultTitle.setSpacingAfter(10);
                document.add(resultTitle);
                
                org.jsoup.nodes.Document doc = Jsoup.parse(resultadosHtml);
                org.jsoup.select.Elements rows = doc.select("tr");
                
                if (!rows.isEmpty()) {
                    PdfPTable resultadosTable = new PdfPTable(6);
                    resultadosTable.setWidthPercentage(100);
                    float[] columnWidths = {1f, 1f, 1f, 1f, 1f, 1f};
                    resultadosTable.setWidths(columnWidths);
                    resultadosTable.setSpacingBefore(10);
                    
                    // Encabezados
                    for (org.jsoup.nodes.Element th : rows.first().select("th")) {
                        PdfPCell cell = new PdfPCell(new Phrase(processText(th.text()), normalFont));
                        cell.setBackgroundColor(COLOR_FONDO_TABLA);
                        cell.setBorderColor(COLOR_BORDE);
                        cell.setPadding(8);
                        resultadosTable.addCell(cell);
                    }
                    
                    // Datos
                    for (int i = 1; i < rows.size(); i++) {
                        org.jsoup.nodes.Element row = rows.get(i);
                        for (org.jsoup.nodes.Element td : row.select("td")) {
                            PdfPCell cell = new PdfPCell(new Phrase(processText(td.text()), smallFont));
                            cell.setBorderColor(COLOR_BORDE);
                            cell.setPadding(6);
                            resultadosTable.addCell(cell);
                        }
                    }
                    
                    document.add(resultadosTable);
                    document.add(Chunk.NEWLINE);
                }
            }
            
            // Detalles de Subredes
            if (subredesHtml != null && !subredesHtml.isEmpty()) {
                Paragraph subTitle = new Paragraph("Detalle de Subredes", seccionFont);
                subTitle.setSpacingAfter(10);
                document.add(subTitle);
                
                // Agregar leyenda de colores
                PdfPTable leyenda = new PdfPTable(3);
                leyenda.setWidthPercentage(60);
                leyenda.setSpacingBefore(10);
                leyenda.setSpacingAfter(15);
                
                // RED
                PdfPCell redLeyenda = new PdfPCell(new Phrase("RED", smallFont));
                redLeyenda.setBackgroundColor(new BaseColor(227, 242, 253));
                redLeyenda.setBorderColor(COLOR_BORDE);
                redLeyenda.setPadding(5);
                leyenda.addCell(redLeyenda);
                
                // SUBRED
                PdfPCell subredLeyenda = new PdfPCell(new Phrase("SUBRED", smallFont));
                subredLeyenda.setBackgroundColor(new BaseColor(255, 235, 238));
                subredLeyenda.setBorderColor(COLOR_BORDE);
                subredLeyenda.setPadding(5);
                leyenda.addCell(subredLeyenda);
                
                // HOST
                PdfPCell hostLeyenda = new PdfPCell(new Phrase("HOST", smallFont));
                hostLeyenda.setBackgroundColor(new BaseColor(241, 248, 233));
                hostLeyenda.setBorderColor(COLOR_BORDE);
                hostLeyenda.setPadding(5);
                leyenda.addCell(hostLeyenda);
                
                document.add(leyenda);
                
                org.jsoup.nodes.Document doc = Jsoup.parse(subredesHtml);
                org.jsoup.select.Elements subreds = doc.select("div.subred-line");
                
                for (org.jsoup.nodes.Element subred : subreds) {
                    Paragraph p = new Paragraph();
                    
                    // Obtener la tabulación original
                    org.jsoup.select.Elements dots = subred.select("span");
                    int numDots = 0;
                    for (org.jsoup.nodes.Element dot : dots) {
                        if (dot.text().equals("・")) {
                            numDots++;
                        }
                    }
                    
                    // Aplicar tabulación
                    if (numDots > 0) {
                        String tabs = "・ ".repeat(numDots);
                        p.add(new Chunk(tabs, monospaceFont));
                    }
                    
                    // Procesar y agregar el texto
                    String infoHtml = subred.html();
                    String cleanInfo = infoHtml.replaceAll("&nbsp;<span>・</span>&nbsp;", "").trim();
                    String plainText = Jsoup.clean(cleanInfo, "", Safelist.none(), new OutputSettings().prettyPrint(false));
                    p.add(new Chunk(processText(plainText), normalFont));
                    
                    document.add(p);
                }
            }
            
            document.close();
            
        } catch (DocumentException e) {
            throw new ServletException("Error al generar el PDF", e);
        }
    }
}
