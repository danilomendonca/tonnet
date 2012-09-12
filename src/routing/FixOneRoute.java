package routing;

import java.io.FileNotFoundException;
import java.util.Vector;
import root.Printer;
import network.Link;
import network.Node;
import network.Pair;

public class FixOneRoute
    extends RoutingWithoutSurvival {

  public FixOneRoute(Vector<Node> nodeList, Vector<Link> linkList,
      Vector<Pair> pairList) {
    super(nodeList, linkList);

    this.routeList = new Vector<Route> (pairList.size());
    this.computeAllFixedRoutes(pairList);
  }

//----------------------------------------------------------------------------
  /**
   * Computa a rota de menor caminho para cada par (o,d) e armazena no routeList.
   * Roteamento fixo de menor caminho (Dijkstra) utilizando o custo de cada enlace.
   * Se todos custos forem iguais, consequentemente o custo da rota será o menor nº de saltos.
   * @param pairList Vector
   */
  private void computeAllFixedRoutes(Vector<Pair> pairList) {
    try {
      Printer out = new Printer(
          ".\\files\\fixedRoutes.txt",false);
      int index = 0;
      for (int i = 0; i < pairList.size(); i++) {
        this.routeList.add(this.shortestPath(pairList.get(i).getSource(),
                                             pairList.get(i).getDestination()));
        out.printlnPath(routeList.get(index).getNodeList());
        index++;
      }
      out.closeFile();
    }
    catch (FileNotFoundException ex) {
      System.err.println(
          "ERRO: classe com o arquivo file\routes.txt. FixedRouting.computeAllRoutes() ");
    }
  }

//............................................................................
  /**
   * Retorna uma rota fixa.
   * @param p Pair
   * @return Route
   */
  public Vector<Route> getRoutes(Pair p) {
    Vector<Route> routes = new Vector<Route> (1);
    routes.add(this.routeList.get(p.getId()));
    return routes;
  }

}
