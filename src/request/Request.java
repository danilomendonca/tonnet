package request;

import network.*;
import routing.Route;
import simulator.EventMachine;

public class Request
    extends RequestMother {

  public Request(Pair p, Mesh mesh) {
    super(p, mesh);
  }

  //------------------------------------------------------------------------------
  /**
   * Roteamento Fixo Sem proteção com conversao total
   * Verifica se ha possibilidade de atender a requisicao
   * e seta a rota a ser utilizada
   * @return boolean
   */
  protected boolean routing() {
    Route route;
    route = this.mesh.getRoutingControl().getRoutes(this.pair).get(0);
    if (route != null) {
      this.setRoute(route);
      return this.route.tryEstablish(this.waveList, this.mesh,
                                     this.pair.getCategory(), true);
    }
    return false;
  }

  //------------------------------------------------------------------------------
  /**
   *Define que os comprimentos de onda alocados ficam livres nos
   *enlaces da rota utilizada pela requisição.
   */
  public void tearDownConnection() {
    for (int i = 0; i < this.waveList.length - 1; i++) {
      if (this.waveList[i] != this.waveList[i + 1]) {
        if (!this.route.getNode(i + 1).liberateWC()) {
          System.err.println("erro ao liberar conversor");
        }
      }
    }

    this.route.liberateWavelength(this.waveList);
  }

//------------------------------------------------------------------------------
  /**
   *Define que os comprimentos de onda alocados ficam ocupados nos
   *enlaces da rota utilizada pela requisição.
   */
  protected void establishConnection() {
    for (int i = 0; i < this.waveList.length - 1; i++) {
      if (this.waveList[i] != this.waveList[i + 1]) {
        if (!this.route.getNode(i + 1).useWc()) {
          System.err.println("erro ao usar conversor");
        }
      }
    }
    if (!this.route.useWavelength(this.waveList)) {
      route.printRoute();
      System.out.println();
      for (int i = 0; i < this.waveList.length; i++) {
        System.out.print(waveList[i] + ";");
      }
    }
  }

  /**
   * retorna uma nova requisição do mesmo tipo
   * @param p Pair
   * @param m Mesh
   * @return RequestMother
   */
  public RequestMother getNewRequest(Pair p, Mesh m) {
    return new Request(p, m);

  }

  public boolean survive(Link link, EventMachine eMachine) {
    return false;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna true se rota for afetada pela falha do link.
   * @param link Link
   * @return boolean
   */
  public boolean requestAffected(Link link) {
    return this.route.containThisLink(link);
  }

}
