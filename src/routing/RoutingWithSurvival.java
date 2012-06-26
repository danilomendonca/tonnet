package routing;

import java.util.Vector;
import network.Link;
import network.Node;
import network.Pair;

public abstract class RoutingWithSurvival
    extends RoutingMother {

  /**
   * Lista com k rotas disjuntas para cada par.
   */
  protected Vector<Vector<Route>> kDisjointRoutesList;
  /**
   * Numero maximo de Links em uma rota.
   */
  private final int routeLinkNamberMax = 50;

  public RoutingWithSurvival(Vector<Node> nodeList, Vector<Link> linkList) {
    super(nodeList, linkList);
    this.kDisjointRoutesList = new Vector<Vector<Route>> ();
  }

  //------------------------------------------------------------------------------
  /**
   * computa os k menores caminhos disjuntos, retornado um Vector de k rotas.
   * para cada posicao, não encontrando a rota, retorna null.
   * Obs. nao é possível uma rota usar um enlace <a,b> em uma rota primária
   * e o enlace <b,a> em uma rota secundária.
   * @param s Node
   * @param t Node
   * @param k int
   * @return Vector path with k Shortest Path disjoint.
   */
  protected Vector<Route> kDisjointShortestPath(Node s, Node t, int k) {
    Vector<Route> routesDisjoint = new Vector();
    //para guardar os custos reais dos links da rota primaria
    double costs[] = new double[this.routeLinkNamberMax];
    int posLink = 0;

    routesDisjoint.add(shortestPath(s, t));
    k--;

    while (k > 0) {
      //setando custo=-1 nos links (ida e volta) que compoem a(s) rota(s) primaria(s)
      Route r = routesDisjoint.lastElement();
      if (r != null) {
        for (int i = 0; i < (routesDisjoint.lastElement()).size() - 1;
             i++) {
          //ida
          Node auxNode = r.getNode(i);
          costs[posLink] = auxNode.getOxc().linkTo( (r.getNode(i + 1)).getOxc()).
              getCost();
          posLink++;
          auxNode.getOxc().linkTo( (r.getNode(i + 1)).getOxc()).setCost( -1);
          //volta
          auxNode = r.getNode(i + 1);
          Link linkVolta = auxNode.getOxc().linkTo( (r.getNode(i)).getOxc());
          if (linkVolta != null) {
            costs[posLink] = linkVolta.getCost();
            posLink++;
            linkVolta.setCost( -1);
          }
        }
      }

      //invoca shortestPath() com os links da(s) rota(s) primaria(s) alterados
      Route tmp = shortestPath(s, t);

      routesDisjoint.add(tmp);

      k--;
    }

    posLink = 0;
    //setando os custos reais dos links da(s) rota(s) primaria(s)
    for (int i = 0; i < routesDisjoint.size() - 1; i++) {
      Route rout = routesDisjoint.get(i);
      if (rout != null) {
        for (int j = 0; j < rout.size() - 1; j++) {
          //ida
          Node auxNode = rout.getNode(j);
          auxNode.getOxc().linkTo( (rout.getNode(j + 1)).getOxc()).setCost(
              costs[posLink]);
          posLink++;
          //volta
          auxNode = rout.getNode(j + 1);
          Link linkVolta = auxNode.getOxc().linkTo( (rout.getNode(j)).getOxc());
          if (linkVolta != null) {
            linkVolta.setCost(costs[posLink]);
            posLink++;
          }
        }
      }
    }

    return routesDisjoint;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna a menor rota disjunta de route
   * @param route Route
   * @return Route
   */
  protected Route disjointShortestPath(Route route) {
    //para guardar os custos reais dos links da rota primaria (ida e volta)
    double[] costs = new double[route.getHops() * 2];
    int posLink = 0;

    //setando custo=-1 nos links que compoem a rota primaria (ida e volta)
    if (route != null) {
      for (int i = 0; i < route.getLinkList().size(); i++) {
        //configurando ida
        Link linkIda = route.getLinkList().get(i);
       // System.out.println("linkIda="+linkIda.getName());
        costs[posLink] = linkIda.getCost();
        posLink++;
        linkIda.setCost( -1);
        //configurando vota
        Link linkVolta = linkIda.getDestination().linkTo(linkIda.getSource());
        costs[posLink] = linkVolta.getCost();
        posLink++;
        linkVolta.setCost( -1);
      }
    }

    //invoca shortestPath() com os links da rota primaria alterados
    Route routeDisjoint = shortestPath(route.getOrigem(), route.getDestino());

    posLink = 0;
    //setando os custos reais dos links da rota primaria (ida e volta)
    for (int i = 0; i < route.getLinkList().size(); i++) {
      //configurando ida
      Link linkIda = route.getLinkList().get(i);
      linkIda.setCost(costs[posLink]);
      posLink++;
      //configurando volta
      Link linkVolta = linkIda.getDestination().linkTo(linkIda.getSource());
      linkVolta.setCost(costs[posLink]);
      posLink++;
    }
    return routeDisjoint;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna o menor caminho para o par P disjunto do Link
   * @param p Pair
   * @param disjLink Link
   * @return Route
   */
  protected Route disjointShortestPathOfLink(Pair p, Link disjLink) {
    Node s = p.getSource();
    Node t = p.getDestination();
    Route routesDisjoint;
    double linkCost; //para guardar o custo real do link

    //setando custo=-1 no link disjLink
    linkCost = disjLink.getCost();
    disjLink.setCost( -1);

    routesDisjoint = shortestPath(s, t);

    //setando o custo real do link disjLink
    disjLink.setCost(linkCost);

    return routesDisjoint;
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna a menor rota do Vector de rotas
   * @param routes Vector
   * @return Route
   */
  protected Route lessCostRoute(Vector<Route> routes) {
    Route route = routes.firstElement();
    for (int i = 0; i < routes.size(); i++) {
      if (routes.get(i)==null)
        break;
      if (routes.get(i).size() < route.size()) {
        route = routes.get(i);
      }
    }

    return route;
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna todos os pares de rota de todos os pares.
   * @return Vector
   */
  public Vector<Vector<Route>> getkDisjointRoutesForAllPairs() {
    return this.kDisjointRoutesList;
  }

}
