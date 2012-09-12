package routing;

import java.util.Vector;
import network.*;
import root.Printer;
import java.io.FileNotFoundException;

public class FixLLROneRoute
    extends RoutingWithoutSurvival {

  /**
   * Valor da utilizacao que define o tipo de roteamento a ser utilizado..
   * Se utilização da rede for menor que 'utilizationPoint' utiliza rot. fixo,
   * Senão roteamento Adaptativo.
   */
  private int utilizationPoint;

  public FixLLROneRoute(Vector<Node> nodeList, Vector<Link> linkList,
      Vector<Pair> pairList) {
    super(nodeList, linkList);

    this.computeAllFixedRoutes(pairList);
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
   * Retorna uma rota fixa ou adaptativa(LEAST LOADED ROUTING)
   * em função da utilização da rede.
   * @param p Pair
   * @return Vector
   */
  public Vector<Route> getRoutes(Pair p) {
    Vector<Route> routes = new Vector<Route> (1);
    if (this.calculateUtilization() < this.utilizationPoint) {
      routes.add(this.routeList.get(p.getId()));
    }
    else {
      routes.add(this.leastLoadedRouting(p));
    }

    return routes;
  }

}
