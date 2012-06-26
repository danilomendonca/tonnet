package routing;

import java.util.Vector;
import network.Link;
import network.Node;
import network.Pair;

public class FixDoubleRoute
    extends RoutingWithSurvival {

  public FixDoubleRoute(Vector<Node> nodeList, Vector<Link> linkList,
      Vector<Pair> pairList) {
    super(nodeList, linkList);

    this.computeAllKFixedRoutes(2, pairList);
  }

  //----------------------------------------------------------------------------
  /**
   * Computa as k rotas disjuntas de menor caminho para cada par (o,d) e
   * armazena em krouteList.
   * @param k int
   * @param pairList Vector
   */
  private void computeAllKFixedRoutes(int k, Vector<Pair> pairList) {
    for (int i = 0; i < pairList.size(); i++) {
      if (!pairList.get(i).getSource().getName().equalsIgnoreCase(pairList.get(
          i).getDestination().getName())) {
        this.kDisjointRoutesList.add(this.kDisjointShortestPath(pairList.get(i).
            getSource(),
            pairList.get(i).getDestination(), k));
      }
    }
  }

  //............................................................................
  /**
   * Retorna as duas menores rotas disjuntas.
   * @param p Pair
   * @return Route[]
   */
  public Vector<Route> getRoutes(Pair p) {
    Vector<Route> routes = new Vector<Route> (2);
    routes.add(this.kDisjointRoutesList.get(p.getId()).get(0));
    routes.add(this.kDisjointRoutesList.get(p.getId()).get(1));
    return routes;
  }

}
