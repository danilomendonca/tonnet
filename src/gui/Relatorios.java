/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.JFreeChart;



/**
 *
 * @author Danil0F
 */
public class Relatorios {

    private Map <String, JFreeChart> chartsGenerated = new HashMap<String, JFreeChart>();

    /**
    * Save chart as PDF file. Requires iText library.
    *
    * @param chart JFreeChart to save.
    * @param fileName Name of file to save chart in.
    * @param width Width of chart graphic.
    * @param height Height of chart graphic.
    * @throws Exception if failed.
    * @see <a href="http://www.lowagie.com/iText">iText</a>
    */
    public void creatReport(String fileName, String title) throws Exception {
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(fileName));

            //convert chart to PDF with iText:
            Rectangle pagesize = new Rectangle(PageSize.A4);

            com.lowagie.text.Document document = new com.lowagie.text.Document(pagesize, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.addAuthor(title);
            document.open();

            //Cabeçalho
            document.add(new Paragraph("Relatório da Simulação"));
            PdfContentByte cb = writer.getDirectContent();
            
            short c = 0;
            for(String chartTitle : chartsGenerated.keySet()){

                JFreeChart chart = chartsGenerated.get(chartTitle);
                if (chart != null) {

                        PdfTemplate tp = cb.createTemplate(pagesize.width(), pagesize.height());
                            Graphics2D g2 = tp.createGraphics(pagesize.width()-20, pagesize.height() - c*420 - 100, new DefaultFontMapper());

                            Rectangle2D r2D = new Rectangle2D.Double(0, 0, 500, 400);
                            chart.draw(g2, r2D, null);
                            g2.dispose();
                            cb.addTemplate(tp, 0, 0);
                            c++;
                    }
            }//else: input values not availabel

           document.close();
         } finally {

                    if (out != null) {
                        out.close();
                    }
        }
    }

    public void addChart(String title, JFreeChart chart){

        chartsGenerated.put(title, chart);
    }

}
