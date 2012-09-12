package routing;

import java.util.Vector;
import network.Pair;
import network.Link;
import network.Node;

public class LLROneRoute
    extends RoutingWithoutSurvival {

  public LLROneRoute(Vector<Node> nodeList, Vector<Link> linkList) {
    super(nodeList, linkList);

  }

  //----------------------------------------------------------------------------
  /**
   * retorna a rota menos carregada para o par p.
   * @param p Pair
   * @return Route
   */
  private Route leastLoadedRouting(Pair p) {
    //acessando todos os links para atualizar os custos
    for (int i = 0; i < this.linkList.size(); i++) {
      double aux = this.linkList.get(i).numWaveBusy();
      if (aux == 0) {
        aux = 0.5;
      }
      this.linkList.get(i).setCost(aux);
    }

    //invoca o dijkstra para recalcular a menor rota
    return this.shortestPath(p.getSource(), p.getDestination());
  }

  //............................................................................
  /**
   * Retorna a menor rota adaptativa cujo custo é a atual utilização do enlaces
   * @param p Pair
   * @return Route
   */
  public Vector<Route> getRoutes(Pair p) {
    Vector<Route> routes = new Vector<Route> (1);
    routes.add(this.leastLoadedRouting(p));
    return routes;
  }

}
