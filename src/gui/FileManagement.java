package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import network.Link;
import network.Node;
import network.Pair;
import org.jdesktop.swingx.JXMapViewer;
import root.Printer;
import simulator.Simulation;

public class FileManagement {
    private String simulationUrl;
    private Printer outSim;
    private Printer outNet;
    private Printer outPairs;
    private Vector<NoGrf> listNodes;
    private Vector<Pair> listPair;
    private JXMapViewer map;
    
    public FileManagement(String simulationUrl) throws FileNotFoundException {
        this.simulationUrl = simulationUrl;
        this.listPair = new Vector();
    }
    
    public void setSimulationUrl(String simulationUrl) {
        this.simulationUrl = simulationUrl;
    }
    
    public void geraArquivoNetwork(String conversionType){
        try {
            this.outNet = new Printer(("Files/"+this.simulationUrl + "/network.net"), true);
        } catch (FileNotFoundException ex) {
           JOptionPane.showMessageDialog(null,"Erro nos arquivos do sistema!","Erro",JOptionPane.ERROR_MESSAGE);
        }
        this.outNet.println("//");
        this.outNet.println(conversionType);
        for (int i = 0; i < this.listNodes.size(); i++) {
            Node n = listNodes.get(i);
            int numWc = 0;
            if (n.getWcBank()!=null){
                numWc = n.getWcBank().getNumWcs();
            }
            this.outNet.print(n.getName() + ";" + n.getPrivilege() + ";" + numWc+";");
            this.outNet.println(((NoGrf)n).getX()+";"+((NoGrf)n).getY());
        }
        
        this.outNet.println("links");
        for (int i = 0; i < this.listNodes.size(); i++) {
            Node n = listNodes.get(i);
            Vector<Link> links = n.getOxc().getLinksList();
            for (int j = 0; j <links.size(); j++) {
                Link l = links.get(j);
                outNet.println(l.getSource().getName()+ ";" + l.getDestination().getName() + ";" +l.getCost()+";"+l.getNumWave());
            }
        }
        this.outNet.println("map");
        this.outNet.print(map.getCenterPosition().getLatitude()+";"+map.getCenterPosition().getLongitude()+";"+map.getZoom());

    }
    
    public void geraArquivoPair() {
        try {
            this.outPairs = new Printer(("Files/"+simulationUrl + "/pairs.prs"), true);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Erro nos arquivos do sistema!","Erro",JOptionPane.ERROR_MESSAGE);
        }
        if (this.listPair.size()>0){
            outPairs.println();
        }
        for (int i = 0; i < this.listPair.size(); i++) {
            Pair p = (Pair) listPair.get(i);
            outPairs.println(p.getSource().getName() + ";"
                    + p.getDestination().getName() + ";" + p.getCategory()+";" + p.getPrivilege());
        }
    }
    
    public void gerarArquivoSimulacao(Simulation s, String incLoad, String points, float hurstMin, float hurstMax, String replyNumber, double significativeLevel, String trafficType, String switchingType, boolean failure, double fixLinkRate, double occurRate ){
        try {
            this.outSim = new Printer(("Files/"+simulationUrl + "/simulation.sim"), true);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Erro nos arquivos do sistema!","Erro",JOptionPane.ERROR_MESSAGE);
        }
        outSim.println("//");
        outSim.println(s.getHoldRate()+";"+s.getArrivedRate()+";"+hurstMin+";"+hurstMax+";"+s.getTotalNumberOfRequest()+";"+s.getSimulationType()+";"+s.getWAAlgorithm());
        outSim.println(incLoad+";"+points+";"+replyNumber);
        outSim.println(trafficType+";"+switchingType);        
        outSim.println(significativeLevel+";");
        outSim.println(failure+";"+fixLinkRate+";"+occurRate);
    }
        
    public void setMap(JXMapViewer map) {
        this.map = map;
    }
    
    public void setListPair(Vector listPair) {
        this.listPair = listPair;
    }
    
    public void setListNodes(Vector listNodes) {
        this.listNodes = listNodes;
    }
    
    public void closeFiles() {
        
        this.outNet.closeFile();
        this.outSim.closeFile();
        if (this.outPairs!=null)
            this.outPairs.closeFile();
    }
    
    public void simulationSave(String diretorioUrl){
        File diretorio = new File(diretorioUrl);
        if(diretorio.mkdir())
            System.out.println("Diretorio criado com sucesso");
        else
            System.out.println("Nao foi possivel criar o diretorio");
        
    }
    public static void copyDirectory(File sourceLocation , File targetLocation)
    throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            
        }
    }
//---Teste---------------------------------------------------------------------------
    public static void main(String[] args) throws Exception {
        JFileChooser jFileChooser1 = new JFileChooser();
        int a = jFileChooser1.showSaveDialog(null);
        File targetLocation = jFileChooser1.getSelectedFile();
      //  teste = fileName1.getAbsolutePath();
        
        File sourceLocation = new File("Files/SimTemp/");
       // File targetLocation = new File("Files/Simulacao/");
        
        copyDirectory(sourceLocation,targetLocation);
    /*Node n1 = new Node("1");
    Node n2 = new Node("2");
    Node n3 = new Node("3");
    FileManagement fm = new FileManagement("Files/SimTemp/");
    Vector lp = new Vector();
    Vector ln = new Vector();
     
    ln.add(n1);
    ln.add(n2);
    ln.add(n3);
     
    lp.add(new Pair(n1, n2));
    lp.add(new Pair(n1, n3));
    lp.add(new Pair(n2, n3));
    lp.add(new Pair(n2, n1));
    lp.add(new Pair(n3, n1));
    lp.add(new Pair(n3, n2));
     
    fm.setListPair(lp);
    fm.setListNodes(ln);
    fm.geraArquivoPair();
    fm.geraArquivoNetwork("without conversion");
    //fm.gerarArquivoSimulacao();
    fm.closeFiles();*/
    }
    
}