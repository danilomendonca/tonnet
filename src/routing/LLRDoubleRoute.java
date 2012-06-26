package routing;

import network.Pair;
import java.util.Vector;
import network.Link;
import network.Node;

public class LLRDoubleRoute
    extends RoutingWithSurvival {

  public LLRDoubleRoute(Vector<Node> nodeList, Vector<Link> linkList) {
    super(nodeList,linkList);

  }
  //----------------------------------------------------------------------------
  /**
   * retorna a rota menos carregada para o par p.
   * @return Route
   * @param pair P
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

  //----------------------------------------------------------------------------
  /**
   * atualiza os custos dos enlaces e retorna a rota disjunta menos carregada
   * @param route Route
   * @return Route
   */
  private Route leastLoadedRoutingDisjoint(Route route) {
    //acessando todos os links para atualizar os custos
    for (int i = 0; i < this.linkList.size(); i++) {
      double aux = this.linkList.get(i).numWaveBusy();
      if (aux == 0) {
        aux = 0.5;
      }
      this.linkList.get(i).setCost(aux);
    }
    //invoca o disjointShortestPath para recalcular a menor rota disjunta
    return this.disjointShortestPath(route);
  }

  //............................................................................
  /**
   * Retorna as duas menores rotas disjuntas.
   * o custo é a atual utilização do enlaces.
   * @param p Pair
   * @return Route[]
   */
  public Vector<Route> getRoutes(Pair p) {
    Vector<Route> routes = new Vector<Route>(2);
      routes.add(this.leastLoadedRouting(p));
      if (routes.get(0) != null) {
        routes.add(this.leastLoadedRoutingDisjoint(routes.get(0)));
      }
      else {
        routes.add(null);
    }
    return routes;
  }

}
