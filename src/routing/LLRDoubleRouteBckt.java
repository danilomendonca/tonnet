package routing;

import network.Pair;
import java.util.Vector;
import network.Link;
import network.Node;

public class LLRDoubleRouteBckt
    extends RoutingWithSurvival {

  public LLRDoubleRouteBckt(Vector<Node> nodeList, Vector<Link> linkList) {
    super(nodeList, linkList);

  }

  //------------------------------------------------------------------------------
  /**
   * Retorna as duas menores rotas disjuntas. O custo é a atual utilização do enlaces.
   * Implementa backtracking.
   * @param s Node
   * @param d Node
   * @return Vector
   */
  private Vector<Route> computeBacktrackingLLR(Node s, Node d) {
    //acessando todos os links para atualizar os custos
    for (int i = 0; i < this.linkList.size(); i++) {
      double aux = this.linkList.get(i).numWaveBusy();
      if (aux == 0) {
        aux = 0.5;
      }
      this.linkList.get(i).setCost(aux);
    }
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
        newRouteB = this.disjointShortestPath(newRouteA);
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

      routeBackupAux = new Route(newRouteB.getNodeList());
    }
    Vector<Route> rotas = new Vector<Route> ();
    rotas.add(routeAux);
    rotas.add(routeBackupAux);
    return rotas;

  }

  //............................................................................
  /**
   * Retorna as duas menores rotas disjuntas. O custo é a atual utilização do enlaces.
   * Implementa backtracking.
   * @param p Pair
   * @return Route[]
   */
  public Vector<Route> getRoutes(Pair p) {
    return this.computeBacktrackingLLR(p.getSource(), p.getDestination());
  }

}
