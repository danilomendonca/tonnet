package gui;

import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import measurement.LinkMeasure;
import measurement.Measurements;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart
        extends JFrame {
    //------------------------------------------------
    private Paint[] cores;
    //private Vector rota;
    //private Vector total;
    private JPanel jPanel1 = new JPanel();
    private Main gui;
    // private double tabela[][];
    private boolean legend=false;
    private int typeChart=0;
    public static Map<Integer, ArrayList<XYDataset>> pastDataSet;

//----------------------------------------------------------

    public Chart (String title){

        super("TONetS Graphics - "+title);
    }

    static {

        pastDataSet = new TreeMap<Integer, ArrayList<XYDataset>>();
    }
    
    /**
     * Creates a new demo.
     * @param title  the frame title.
     */
    public JFreeChart getChart(String title, boolean frame, Main gui, int tipo,Vector<Vector<Measurements>> m) {
      
        this.gui = gui;
        //if (te.tred!= null)
        
        String xLabel="";
        String yLabel="";
        if (title.equals("Probabilidade de Bloqueio Geral")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Probabilidade de Bloqueio Geral";
            
        }else if (title.equals("PB por Ausência de Backup")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Probabilidade de Bloqueio por Ausência de Backup";
            
        }else if ((title.equals("PB por Par(Origem, Destino)"))||(title.equals("Probabilidade de Bloqueio por Par"))) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Probabilidade de Bloqueio";
            legend=true;
            
        }else if (title.equals("Utilização Geral")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Utilização Geral da Rede";
            
        }else if (title.equals("Utilização por Link")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Utilização Média";
            legend=true;
            typeChart=1;
            
        }else if (title.equals("Utilização por Comprimento de Onda")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Utilização por Comprimento de Onda";
            legend=true;
            typeChart=1;
            
        }else if (title.equals("Restaurabilidade")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Restaurabilidade";
            
        }else if (title.equals("TM das Requisições Atendidas")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Tamanho Médio das Requisições Atendidas";
            
        }else if (title.equals("TM das Requisições Bloqueadas")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Tamanho Médio das Requisições Bloqueadas";
            
        }else if (title.equals("Número de Falhas por Link")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Número de Falhas";
            legend=true;
            typeChart=1;
            
        }else if (title.equals("Número de Rotas por Link")) {
            xLabel="Link";
            yLabel="Número de Rotas";
            legend=false;
            typeChart=2;
            
        }else if (title.equals("Número de Requisições por Par")) {
            xLabel="Intensidade de Tráfego Gerada na Rede em Erlang";
            yLabel="Número de Requisições";
            legend=true;
            typeChart=1;
        }
        
        JFreeChart chart=null;
        if (typeChart==0){//line chart
            XYDataset dataset = createDataset(m,title);
            //title="";
            ArrayList<XYDataset> dataList = null;// pastDataSet.get(typeChart);
            if(dataList == null)
                dataList = new ArrayList();

            /*if(dataList.size() == gui.chartPos - 1)
                dataList.add(gui.chartPos - 1, dataset);
            else
                dataList.set(gui.chartPos - 1, dataset);*/

            dataList.add(dataset);
            
            pastDataSet.put(typeChart, dataList);
            XYDataset [] dataArray = dataList.toArray(new XYDataset[0]);
            chart = createChart(title, tipo, xLabel, yLabel, dataArray);
   
        }else if (typeChart==1){//bar chart
            CategoryDataset dataset = createDataset1(m,title);
            // create the chart...
            chart = ChartFactory.createBarChart3D(
                    title,  // chart title
                    xLabel,         // domain axis label
                    yLabel,            // range axis label
                    dataset,           // data
                    PlotOrientation.VERTICAL,
                    legend,               // include legend
                    true,               //tool
                    false               //url
                    );
            
        }else if (typeChart==2){//bar chart 3D
            CategoryDataset dataset = createDataset1(m,title);
            // create the chart...
            chart = ChartFactory.createBarChart(
                    title,  // chart title
                    xLabel,         // domain axis label
                    yLabel,            // range axis label
                    dataset,           // data
                    PlotOrientation.VERTICAL,
                    legend,               // include legend
                    true,               //tool
                    false               //url
                    );            
        }        


        ChartPanel chartPanel2 = new ChartPanel(chart);
      
        if (frame == false) {
            JLayeredPane p = this.gui.getjPanelGraf();
            chartPanel2.setSize(p.getSize());
            p.setEnabled(true);
            this.gui.getjPanelGraf().removeAll();
            p.add(chartPanel2);
        } else {
            chartPanel2.setDomainZoomable(true);
            chartPanel2.setRangeZoomable(true);
            chartPanel2.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel2);
        }

        return chart;
    }
    
    private CategoryDataset createDataset1(Vector<Vector<Measurements>> mainMeasurements,String title) {
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        
        if (mainMeasurements!=null){
            if (title.equals("Utilização por Link")) {
                //numero de links
                int numLinks = mainMeasurements.get(0).get(0).getListLinkMeasure().size();
                //percorre a carga
                for (int c = 0; c < mainMeasurements.size(); c++) {
                    //parametro=nome da série
                    String serieC = mainMeasurements.get(c).get(0).getArrivedRate()+"";
                    //percorre a lista de links da carga (c)
                    for (int l = 0; l < numLinks; l++) {
                        double sumUtiliLink = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações do Link (l)
                        for (int r = 0; r < numReplication; r++) {
                            //soma utilizacao (replicações)
                            sumUtiliLink += mainMeasurements.get(c).get(r).getListLinkMeasure().
                                    get(l).
                                    getUtilization();
                        }
                        
                        //calcula a a utilização media do link
                        double averageUtil = sumUtiliLink / numReplication;
                        
                        // double x = mainMeasurements.get(c).get(0).getListLinkMeasure().
                        //           get(l).getName();
                        double y=averageUtil;
                        dataset.addValue(y,mainMeasurements.get(c).get(0).getListLinkMeasure().get(l).getName(),serieC);
                    }
                    
                }
                
            }else if (title.equals("Utilização por Comprimento de Onda")) {
                //numero de comprimentos de onda
                int numWaves = mainMeasurements.get(0).get(0).getLinkList().get(0).getNumWave();
                //percorre a carga
                for (int c = 0; c < mainMeasurements.size(); c++) {
                    //parametro=nome da série
                    String seriesC = mainMeasurements.get(c).get(0).getArrivedRate()+"";
                    //percorre a lista de links de comprimentos de onda da carga (c)
                    for (int w = 0; w < numWaves; w++) {
                        double sumWaveUtil = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações da carga (c)
                        for (int r = 0; r < numReplication; r++) {
                            //soma utilizacao do comprimento de onda (replicações)
                            sumWaveUtil += mainMeasurements.get(c).get(r).
                                    getWavelenghUtilization()[w];
                        }
                        //calcula a a utilização media do comprimento de onda
                        double averageUtil = sumWaveUtil / numReplication;
                        double y=averageUtil;
                        dataset.addValue(y,w+"",seriesC);
                    }
                }
            }else if(title.equals("Número de Falhas por Link")){
                //numero de links
                int numLinks = mainMeasurements.get(0).get(0).getListLinkMeasure().size();
                //percorre a carga
                for (int c = 0; c < mainMeasurements.size(); c++) {
                    //exibe a carga c
                    //parametro=nome da série
                    String seriesC = mainMeasurements.get(c).get(0).getArrivedRate()+"";
                    //percorre a lista de links da carga (c)
                    for (int l = 0; l < numLinks; l++) {
                        double sumFailLink = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações do Link (l)
                        for (int r = 0; r < numReplication; r++) {
                            //soma as replicações
                            sumFailLink +=
                                    mainMeasurements.get(c).get(r).getListLinkMeasure().get(l).
                                    getNumberFailure();
                        }
                        
                        //calcula o numero media de falhas no link
                        double averageFail = sumFailLink / numReplication;
                        double y = averageFail;
                        dataset.addValue(y,mainMeasurements.get(c).get(0).getListLinkMeasure().get(l).getName(),seriesC);
                    }
                }
                
                
            }else if(title.equals("Número de Rotas por Link")){
                //numero de links
                int numLinks = mainMeasurements.get(0).get(0).getListLinkMeasure().size();
                //percorre a lista de links(Measure) da primeira simulação
                for (int l = 0; l < numLinks; l++) {
                    LinkMeasure linkMeasure = mainMeasurements.get(0).get(0).getListLinkMeasure().get(l);
                    double y = linkMeasure.getRoutes().size();
                    dataset.addValue(y,"",linkMeasure.getName());
                }
                
            }else if(title.equals("Número de Requisições por Par")){
                //numero de pares
                int numOfPair = mainMeasurements.get(0).get(0).getListPairMeasure().size();
                //percorre a lista de pares
                for (int p = 0; p < numOfPair; p++) {
                    //nome do par
                    String categPar = mainMeasurements.get(0).get(0).getListPairMeasure().get(p).getName();
                    //percorre a carga para o par p
                    for (int c = 0; c < mainMeasurements.size(); c++) {
                        String seriesC = mainMeasurements.get(c).get(0).getArrivedRate()+"";
                        double sumReqPair = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações do par (p)
                        for (int r = 0; r < numReplication; r++) {
                            //soma as requisições de todas replicações do par p para carga c
                            sumReqPair +=
                                    mainMeasurements.get(c).get(r).getListPairMeasure().get(p).
                                    getNumberReq();
                        }
                        //calcula o numero medio de requisições do par p para carga c
                        double averageReq = sumReqPair / numReplication;
                        //grava o numero medio de requisições do par p
                        double y = averageReq;
                        dataset.addValue(y,categPar,seriesC);
                    }
                    
                }
                
            }
        }
        return dataset;
    }
    
//-------------------------
    /**
     * Creates a sample dataset.
     * @return a sample dataset.
     */
    public XYDataset createDataset(Vector<Vector<Measurements>> mainMeasurements,String title) {
        //nome da série = algorimo WA
        String algo = gui.getWAssingAlgo();
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        if (mainMeasurements != null) {
            
            XYSeries series1 = new XYSeries(algo);//parametro=nome da série
            
            if (title.equals("Probabilidade de Bloqueio Geral")) {
                //percorre a carga
                for (int i = 0; i < mainMeasurements.size(); i++) {
                    double sumPB = 0;
                    int numReplication = mainMeasurements.get(i).size();
                    //para calculo da media
                    double[] pb = new double[numReplication];
                    //percorre as replicações para carga i
                    for (int j = 0; j < numReplication; j++) {
                        //soma das replicações
                        pb[j] = mainMeasurements.get(i).get(j).getPb();
                        sumPB += pb[j];
                    }
                    double mediaPB = sumPB / numReplication;
                    //carga i
                    double x = mainMeasurements.get(i).get(0).getArrivedRate();
                    //Pb Bloqueio Médio
                    double y=mediaPB;
                    series1.add(x,y);
                }
                
            }else if (title.equals("PB por Ausência de Backup")) {
                //percorre a carga
                for (int i = 0; i < mainMeasurements.size(); i++) {
                    double sumPB = 0;
                    int numReplication = mainMeasurements.get(i).size();
                    //para calculo da media
                    double[] pb = new double[numReplication];
                    //percorre as replicações para carga i
                    for (int j = 0; j < numReplication; j++) {
                        //soma das replicações
                        pb[j] = mainMeasurements.get(i).get(j).getPbBlockingAbsenceBackupProtection();
                        sumPB += pb[j];
                    }
                    double mediaPB = sumPB / numReplication;
                    //carga i
                    double x = mainMeasurements.get(i).get(0).getArrivedRate();
                    //Pb Bloqueio Médio
                    double y=mediaPB;
                    series1.add(x,y);
                }
                
            }else if (title.equals("Utilização Geral")) {
                
                //percorre a carga
                for (int i = 0; i < mainMeasurements.size(); i++) {
                    double sumPB = 0;
                    int numReplication = mainMeasurements.get(i).size();
                    //para calculo da media
                    double[] pb = new double[numReplication];
                    //percorre as replicações para carga i
                    for (int j = 0; j < numReplication; j++) {
                        //soma das replicações
                        pb[j] = mainMeasurements.get(i).get(j).getUtilization();
                        sumPB += pb[j];
                    }
                    double mediaPB = sumPB / numReplication;
                    //carga i
                    double x = mainMeasurements.get(i).get(0).getArrivedRate();
                    //Pb Bloqueio Médio
                    double y=mediaPB;
                    series1.add(x,y);
                }
                
            }else if (title.equals("Utilização por Link")) {
                //numero de links
                int numLinks = mainMeasurements.get(0).get(0).getListLinkMeasure().size();
                //percorre a carga
                for (int c = 0; c < mainMeasurements.size(); c++) {
                    //parametro=nome da série
                    series1 = new XYSeries(mainMeasurements.get(c).get(0).getArrivedRate()+"");
                    //percorre a lista de links da carga (c)
                    for (int l = 0; l < numLinks; l++) {
                        double sumUtiliLink = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações do Link (l)
                        for (int r = 0; r < numReplication; r++) {
                            //soma utilizacao (replicações)
                            sumUtiliLink += mainMeasurements.get(c).get(r).getListLinkMeasure().
                                    get(l).
                                    getUtilization();
                        }
                        
                        //calcula a a utilização media do link
                        double averageUtil = sumUtiliLink / numReplication;
                        
                        // double x = mainMeasurements.get(c).get(0).getListLinkMeasure().
                        //           get(l).getName();
                        double y=averageUtil;
                        series1.add(l,y);
                    }
                    if (c<mainMeasurements.size()-1){
                        dataset.addSeries(series1);
                    }
                }
                
            }else if (title.equals("Utilização por Comprimento de Onda")) {
                //numero de comprimentos de onda
                int numWaves = mainMeasurements.get(0).get(0).getLinkList().get(0).getNumWave();
                //percorre a carga
                for (int c = 0; c < mainMeasurements.size(); c++) {
                    //parametro=nome da série
                    series1 = new XYSeries(mainMeasurements.get(c).get(0).getArrivedRate()+"");
                    //percorre a lista de links de comprimentos de onda da carga (c)
                    for (int w = 0; w < numWaves; w++) {
                        double sumWaveUtil = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações da carga (c)
                        for (int r = 0; r < numReplication; r++) {
                            //soma utilizacao do comprimento de onda (replicações)
                            sumWaveUtil += mainMeasurements.get(c).get(r).
                                    getWavelenghUtilization()[w];
                        }
                        //calcula a a utilização media do comprimento de onda
                        double averageUtil = sumWaveUtil / numReplication;
                        double x = w;
                        double y=averageUtil;
                        series1.add(x,y);
                    }
                    if (c<mainMeasurements.size()-1){
                        dataset.addSeries(series1);
                    }
                }
            }else if (title.equals("Restaurabilidade")) {
                //percorre a carga
                for (int i = 0; i < mainMeasurements.size(); i++) {
                    double sumPB = 0;
                    int numReplication = mainMeasurements.get(i).size();
                    //para calculo da media
                    double[] pb = new double[numReplication];
                    //percorre as replicações para carga i
                    for (int j = 0; j < numReplication; j++) {
                        //soma das replicações
                        pb[j] = mainMeasurements.get(i).get(j).getPbRestorability();
                        sumPB += pb[j];
                    }
                    double mediaPB = sumPB / numReplication;
                    //carga i
                    double x = mainMeasurements.get(i).get(0).getArrivedRate();
                    //Pb Bloqueio Médio
                    double y=mediaPB;
                    series1.add(x,y);
                }
                
            }else if (title.equals("TM das Requisições Atendidas")) {
                //percorre a carga
                for (int i = 0; i < mainMeasurements.size(); i++) {
                    double sumPB = 0;
                    int numReplication = mainMeasurements.get(i).size();
                    //para calculo da media
                    double[] pb = new double[numReplication];
                    //percorre as replicações para carga i
                    for (int j = 0; j < numReplication; j++) {
                        //soma das replicações
                        pb[j] = mainMeasurements.get(i).get(j).getAverageSizeOfPrimaryAcceptedReq();
                        //pb[j] += mainMeasurements.get(i).get(j).getAverageSizeOfBackupAcceptedReq();
                        sumPB += pb[j];
                    }
                    double mediaPB = sumPB / numReplication;
                    //carga i
                    double x = mainMeasurements.get(i).get(0).getArrivedRate();
                    //Pb Bloqueio Médio
                    double y=mediaPB;
                    series1.add(x,y);
                }
                
                
            }else if (title.equals("TM das Requisições Bloqueadas")) {
                //percorre a carga
                for (int i = 0; i < mainMeasurements.size(); i++) {
                    double sumPB = 0;
                    int numReplication = mainMeasurements.get(i).size();
                    //para calculo da media
                    double[] pb = new double[numReplication];
                    //percorre as replicações para carga i
                    for (int j = 0; j < numReplication; j++) {
                        //soma das replicações
                        pb[j] = mainMeasurements.get(i).get(j).getAverageSizeOfPrimaryBlockedReq();
                        //pb[j] += mainMeasurements.get(i).get(j).getAverageSizeOfBackupBlockedReq();
                        sumPB += pb[j];
                    }
                    double mediaPB = sumPB / numReplication;
                    //carga i
                    double x = mainMeasurements.get(i).get(0).getArrivedRate();
                    //Pb Bloqueio Médio
                    double y=mediaPB;
                    series1.add(x,y);
                }
                
            }else if ((title.equals("PB por Par(Origem, Destino)"))||(title.equals("Probabilidade de Bloqueio por Par"))) {
                //numero de pares
                        int numOfPair = mainMeasurements.get(0).get(0).getListPairMeasure().size();
                //para calculo da justiça
                double[][] average = new double[mainMeasurements.size()][numOfPair];
                //percorre a lista de pares                
                for (int p = 0; p < numOfPair; p++) {
                   //exibe o nome do par p
                    series1 = new XYSeries(mainMeasurements.get(0).get(0).getListPairMeasure().get(p).getName());
                    //percorre a carga
                    for (int c = 0; c < mainMeasurements.size(); c++) {
                        double sumBlockPair = 0;
                        int numReplication = mainMeasurements.get(c).size();
                        //percorre as replicações do par (p) para a carga c
                        for (int r = 0; r < numReplication; r++) {
                            //soma bloqueio
                            sumBlockPair += mainMeasurements.get(c).get(r).getListPairMeasure().
                                    get(p).getPbBlocking();
                        }
                        //calcula o bloqueio medio do par
                        double averageBlock = sumBlockPair / numReplication;                                                
                        average[c][p] = averageBlock;
                        double x = mainMeasurements.get(c).get(0).getArrivedRate();
                        double y = averageBlock;
                        series1.add(x,y);                   
                    }
                    if (p<numOfPair-1){
                        dataset.addSeries(series1);
                    }
                }
            }
            dataset.addSeries(series1);
        }
        
        return dataset;
    }
    
//---------
    /**
     * Creates a chart.
     * @param dataset  the data for the chart.
     * @return a chart.
     */
    public JFreeChart createChart(String title ,int tipo,String xLabel, String yLabel, XYDataset ... datasets ) {
        cores = new Paint[] {Color.lightGray};
// create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // chart title
                xLabel, //x Label
                yLabel,//y Label
                datasets[0], // data
                PlotOrientation.VERTICAL,
                legend, // include legend
                true, // tooltips
                false // urls
                );
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // chart.setBackgroundPaint(Color.white);
        //chart.get
        //StandardLegend legend = (StandardLegend) chart.getLegend();
        //   legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        XYPlot plot = chart.getXYPlot();

        short c = 0;
        for(XYDataset dataset : datasets){
            
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesStroke(c, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {10.0f, 6.0f}, 0.0f));
            if(c > 0)
            renderer.setSeriesPaint(c, getRegularColour(c));
            plot.setDataset(c++, dataset);
           
        }

        plot.setBackgroundPaint(Color.white);
        //plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0)); //Classe Spacer inexistente dfmendonca
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setAutoTickUnitSelection(true);
        plot.getRangeAxis().setAutoTickUnitSelection(true);
        if (tipo != 2) {
            plot.getDomainAxis().setLabelFont(new Font("Times New Romam", Font.PLAIN, 18));
            plot.getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 18));
            //  legend.setItemFont(new Font("SansSerif", Font.PLAIN, 16));
            
        }
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true); //modificado dfmendonca
        renderer.setShapesFilled(true);
        // change the auto tick unit selection to integer units only...
        //NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
        
    }

    private Color getRegularColour(int c){

        switch(c){

            case 0: return Color.ORANGE;
            case 1: return Color.CYAN;
            case 2: return Color.DARK_GRAY;
            case 3: return Color.GRAY;
            case 4: return Color.GREEN;
            case 5: return Color.MAGENTA;
            case 6: return Color.ORANGE;
            case 7: return Color.PINK;
            case 8: return Color.RED;
            case 9: return Color.WHITE;
            case 10: return Color.YELLOW;
            default: return Color.BLACK;
        }
    }
    
    public Chart() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        
    }
    
// ****************************************************************************
// * COMMERCIAL SUPPORT / JFREECHART DEVELOPER GUIDE                          *
// * Please note that commercial support and documentation is available from: *
// *                                                                          *
// * http://www.object-refinery.com/jfreechart/support.html                   *
// *                                                                          *
// * This is not only a great service for developers, but is a VERY IMPORTANT *
// * source of funding for the JFreeChart project.  Please support us so that *
// * we can continue developing free software.                                *
// ****************************************************************************
    
}