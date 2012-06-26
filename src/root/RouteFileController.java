package root;

import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import network.Node;
import routing.Route;

public class RouteFileController {

  public RouteFileController() {
  }

  public static Vector<Route> readFile(String fileName, Vector<Node> nodeList) {
    BufferedReader in;
    FileReader file;
    Vector<Route> routeList = new Vector<Route>();

   try {
     file = new FileReader(fileName);
     in = new BufferedReader(file);
     //LINE 1-lendo os comentarios
     String comment = in.readLine();
     System.out.println(comment);
     //lendo as rotas...
     while (in.ready()) {
       StringTokenizer line = new StringTokenizer(in.readLine(),";\t");
       Vector<Node> nodeRoute = new Vector<Node>();
       while (line.hasMoreElements()) {
         String nodeName = line.nextToken();
         Node node = RouteFileController.getNode(nodeName, nodeList);
         nodeRoute.add(0,node);
       }
       routeList.add(new Route(nodeRoute));
     }

   }
   catch (IOException e) {
     System.out.println("File Error - Sintaxe or file not found");
     System.exit(0);
   }
//   System.out.println("numero de rotas : "+ routeList.size());
   return routeList;
 }

 /**
  * get Node for name
  * @param name String
  * @return Node Node at name.
  */
 private static Node getNode(String name, Vector<Node> nodeList) {
   for (int i = 0; i < nodeList.size(); i++) {
     Node tmp = nodeList.get(i);
     if (tmp.getName().equals(name))
       return tmp;
   }
   return null;
 }

}
