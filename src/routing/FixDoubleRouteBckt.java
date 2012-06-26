package routing;

import java.util.Vector;
import network.Link;
import network.Node;
import network.Pair;

public class FixDoubleRouteBckt
    extends RoutingWithSurvival {

  public FixDoubleRouteBckt(Vector<Node> nodeList, Vector<Link> linkList,
      Vector<Pair> pairList) {
    super(nodeList, linkList);

    this.computeFixedBacktracking(pairList);
  }

  //------------------------------------------------------------------------------
  /**
   * Computa as k rotas disjuntas de menor caminho para cada par (o,d) e
   * armazena em krouteList.Implemeta backtracking.
   * @param pairList Vector
   */
  private void computeFixedBacktracking(Vector<Pair> pairList) {

    for (int i = 0; i < pairList.size(); i++) {
      if (!pairList.get(i).getSource().getName().equalsIgnoreCase(pairList.get(
          i).
          getDestination().getName())) {
        this.kDisjointRoutesList.add(this.computeBacktracking(pairList.get(i).
            getSource(),
            pairList.get(i).getDestination()));
      }
    }
  }

  //----------------------------------------------------------------------------
  /**
   * computeBacktracking
   * @param s Node
   * @param d Node
   * @return Vector
   */
  private Vector<Route> computeBacktracking(Node s, Node d) {
    Route routeAux, routeBackupAux;
    routeAux = this.shortestPath(s, d);
    routeBackupAux = this.disjointShortestPath(routeAux);
    if (routeBackupAux == null) {
      Vector<Route> rotasPrimarias = new Vector<Route> ();
      Vector<Link> linkListWork = routeAux.getLinkList();
      for (int i = 0; i < linkListWork.size(); i++) {
        rotasPrimarias.add(disjointShortestPathOfLink(new
            Pair(routeAux.getOrigem(),
                 routeAux.getDestino()),
            linkListWork.get(i)));
      }

      Route newRouteA = null;
      Route newRouteB = null;
      while ( (newRouteB == null) && (rotasPrimarias.size() > 0)) {
        newRouteA = this.lessCostRoute(rotasPrimarias);
        if (newRouteA != null) {
          newRouteB = this.disjointShortestPath(newRouteA);
        }

        if (newRouteB != null) {
          routeAux = new Route(newRouteA.getNodeList());
        }
        else {
          rotasPrimarias.remove(newRouteA);
        }
      }

      if (newRouteB == null) {
        System.out.println("err");
      }
      else {
        routeBackupAux = new Route(newRouteB.getNodeList());
      }
    }
    Vector<Route> rotas = new Vector<Route> ();
    rotas.add(routeAux);
    rotas.add(routeBackupAux);
    return rotas;
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
