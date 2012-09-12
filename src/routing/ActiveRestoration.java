package routing;

import java.util.Vector;
import network.Link;
import network.Node;
import network.Pair;

public class ActiveRestoration extends RoutingWithSurvival {

  public ActiveRestoration(Vector<Node> nodeList, Vector<Link> linkList,
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

  /**
   * procurar uma rota da origem da rota work até destino do enlace link,
   * distunto do enlace link. Depois concatena com o restante da rota work (
   * destino do enlace link até destino da rota work)
   * @param work Route
   * @param failureLink Link
   * @return Route
   */
  public Route activeRestoration(Route work, Link failureLink) {
    //nó destino do link falho
    Node immediate = this.getNode(failureLink.getDestination().getName());

    //rota backup. rota da origem da rota work ate o nó destino do link falho
    Route backup = disjointShortestPathOfLink(new Pair(work.getOrigem(),
        immediate), failureLink);

    //lista de nós do backup
    if (backup == null) {
      return null;
    }
    else {
      Vector<Node> nodesBackup = backup.getNodeList();

      //lista de nós restantes da rota work
      //(do destino do link falho ate o destino da rota work)
      Vector<Node> nodesRest = new Vector<Node> ();
      Node aux = immediate;
      while (!aux.equals(work.getDestino())) {
        aux = work.getNext(aux);
        nodesRest.add(aux);
      }

      //concatenando backup com nos restantes da rota work
      Vector<Node> restoration = nodesBackup;
      restoration.addAll(nodesRest);

      return (new Route(restoration));
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
