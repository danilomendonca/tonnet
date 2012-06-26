package root;

import gui.LinkGrf;
import gui.NoGrf;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import network.Node;
import org.jdesktop.swingx.JXMapViewer;

public class NodeFileController {

  public NodeFileController() {
  }

  public static Vector[] readFile(String fileName,Vector<Integer> conversionType) {

    return readFile(fileName, conversionType, null);
  }

  @SuppressWarnings("unchecked")
public static Vector[] readFile(String fileName,Vector<Integer> conversionType, JXMapViewer mapa) {
    Vector<Node> listNode = new Vector<Node>();
    Hashtable<String, NoGrf> listNodeGrf = new Hashtable<String, NoGrf>();
    Vector<LinkGrf> listLinkGrf = new Vector<LinkGrf>();
    Hashtable<String,Node> list = new Hashtable<String,Node>();//para 'pegar' um Node pelo nome
    BufferedReader in;
    FileReader file;

    try {
      file = new FileReader(fileName);
      in = new BufferedReader(file);
      //Linha 1-lendo os comentarios
      String comment = in.readLine();
     // System.out.println(comment);

     //Linha 2-lendo o tipo de conversao
     String conversion = in.readLine();
     if (conversion.equalsIgnoreCase("without conversion"))
       conversionType.add(0,0);
     else if (conversion.equalsIgnoreCase("Partial conversion"))
         conversionType.add(0,1);
     else if (conversion.equalsIgnoreCase("esparse conversion"))
         conversionType.add(0,2);
     else if (conversion.equalsIgnoreCase("esparse partial conversion"))
         conversionType.add(0,3);
     else if (conversion.equalsIgnoreCase("full conversion"))
       conversionType.add(0,4);
     else conversionType.add(0,0);

     //Linha 3-lendo os nomes dos nos
      StringTokenizer line = new StringTokenizer(in.readLine(),";\t");
      String token = line.nextToken();
      while (!token.equalsIgnoreCase("links")) {
        String nodeName = token;
        Node tmp = new Node(nodeName);
        tmp.setPrivilege(Integer.valueOf(line.nextToken()));
        list.put(tmp.getName(),tmp);
        listNode.add(tmp);
        
        if (conversionType.get(0)==2){
          int numWCs = Integer.valueOf(line.nextToken());
          if (numWCs > 0)
            tmp.setWcr(numWCs);
        }else
            line.nextToken();//passa a linha
        
        NoGrf tmpGrf = new NoGrf(new Point(Integer.parseInt(line.nextToken()), Integer.parseInt(line.nextToken())), tmp.getName());
        listNodeGrf.put(tmpGrf.getName(), tmpGrf);
        
        
        line = new StringTokenizer(in.readLine(),";\t");
        token = line.nextToken();
      }
      
      int linkId = 0;
      //LINE 3-lendo links...
      while (in.ready()) {
        line = new StringTokenizer(in.readLine(),";\t");
        String nameS = line.nextToken(); //lendo no source
        String nameD = line.nextToken(); //lendo no destino
        //System.out.println(nameS+" - "+nameD);
        double cost = Double.parseDouble(line.nextToken()); //lendo custo do enlace
        int numWave = Integer.parseInt(line.nextToken()); //lendo num wavelegth

        list.get(nameS).getOxc().addLink(list.get(nameD).
            getOxc(), cost, numWave);

        if(Integer.parseInt(nameS) < Integer.parseInt(nameD)){//NAO ESTÁ VERIFICANDO BIDIRECIONALIDE (ARQ NETWORK ESTÁ IMCOMPLETO?), ASSUMINDO TRUE
        	NoGrf nGrf1 = listNodeGrf.get(nameS);
        	NoGrf nGrf2 = listNodeGrf.get(nameD);
        	LinkGrf lGrf = new LinkGrf(linkId++, nGrf1, nGrf2, cost, numWave, true, mapa);
        	nGrf1.getOxc().addLink(nGrf2.getOxc(),cost,numWave);
                nGrf2.getOxc().addLink(nGrf1.getOxc(),cost,numWave);
        	listLinkGrf.add(lGrf);
        }
      }

    }
    catch (IOException e) {
      System.out.println("File Error - Sintaxe or file not found");
      System.exit(0);
    }
    
    return new Vector[]{listNode, new Vector(listNodeGrf.values()), listLinkGrf};
  }


  
}
