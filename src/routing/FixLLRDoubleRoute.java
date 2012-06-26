package routing;

import network.Pair;
import network.Link;
import java.util.Vector;
import network.Node;

public class FixLLRDoubleRoute
    extends RoutingWithSurvival {

  /**
   * Valor da utilizacao que define o tipo de roteamento a ser utilizado..
   * Se utilização da rede for menor que 'utilizationPoint' utiliza rot. fixo,
   * Senão roteamento Adaptativo.
   */
  private int utilizationPoint;

  public FixLLRDoubleRoute(Vector<Node> nodeList, Vector<Link> linkList,
      Vector<Pair> pairList) {
    super(nodeList, linkList);

    this.computeAllKFixedRoutes(2, pairList);
  }

  //----------------------------------------------------------------------------
  /**
   * Configura o ponto de utilização.
   * @param utilizationPoint int
   */
  public void setUtilizationPoint(int utilizationPoint){
    this.utilizationPoint = utilizationPoint;
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

  //----------------------------------------------------------------------------
  /**
   * calcula a utilização da rede. Faz o somatorio do número de comprimentos
   * de onda ocupados em todos os enlaces e divide pelo número total de
   * comprimentos de onda da rede.
   * @return double
   */
  private double calculateUtilization() {
    double waveBusy = 0, totalWaveNumber = 0;

    for (int i = 0; i < this.linkList.size(); i++) {
      waveBusy = waveBusy + linkList.get(i).numWaveBusy();
      totalWaveNumber = totalWaveNumber + linkList.get(i).getNumWave();
    }
    return (waveBusy / totalWaveNumber);
  }

  /**
   * Retorna duas rotas fixa ou adaptativa(LEAST LOADED ROUTING)
   * em função da utilização da rede.
   * @param p Pair
   * @return Vector
   */
  public Vector<Route> getRoutes(Pair p) {
    Vector<Route> routes = new Vector<Route> (2);
    if (this.calculateUtilization() < this.utilizationPoint) {
      routes = this.kDisjointRoutesList.get(p.getId()); // Fixo
    }
    else { //LLR
      routes.add(this.leastLoadedRouting(p));
      if (routes.get(0) != null) {
        routes.add(this.leastLoadedRoutingDisjoint(routes.get(0)));
      }
      else {
        routes.add(null);
      }
    }
    return routes;
  }

}
