package request;

import java.util.Vector;
import network.*;
import simulator.EventMachine;
import routing.Route;
import routing.*;

public class ReqActRestoration
    extends RequestMother {

  private Vector<LinkActiveRestoration> listLinkAR;

  public ReqActRestoration(Pair p, Mesh mesh) {
    super(p, mesh);
    listLinkAR = new Vector<LinkActiveRestoration> ();
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

    this.route.useWavelength(this.waveList);
  }

  //------------------------------------------------------------------------------
  public boolean RWA() {
    boolean establish = false;

    establish = routing();
    if (establish) {
      this.establishConnection();
      // computa as rotas disjuntas para cada enlace da rota de trabalho.
      this.preComputeActiveRestoration();
    }
    return establish;
  }

//------------------------------------------------------------------------------
  protected boolean routing() {
    Route routeAux;
    routeAux = this.mesh.getRoutingControl().getRoutes(this.pair).get(0);
    if (routeAux != null) {
      this.setRoute(routeAux);
      
      boolean hasControlChannel = true;
      controlChannel = new int[1];
      return this.route.tryEstablish(this.waveList, this.mesh,
                                                  this.pair.getCategory(),true);
    }
    return false;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna uma nova requisição do mesmo tipo
   * @param p Pair
   * @param m Mesh
   * @return RequestMother
   */
  public RequestMother getNewRequest(Pair p, Mesh m) {
    return new ReqActRestoration(p, m);
  }

  //------------------------------------------------------------------------------

  /**
   * computa as diferentes rotas para restoração para a possibilide de falha
   * em cada enlace da rota primária.
   */
  private void preComputeActiveRestoration() {
    Vector<Link> linksWorkRoute = this.route.getLinkList();
    Link auxLink;
    Route routeAux;
    for (int i = 0; i < linksWorkRoute.size(); i++) {
      auxLink = linksWorkRoute.get(i);
      routeAux = ((ActiveRestoration)this.mesh.getRoutingControl().getRoutingObject()).activeRestoration(this.route,
          auxLink);
      this.listLinkAR.add(new LinkActiveRestoration(auxLink, routeAux));
    }
  }

  //------------------------------------------------------------------------------
  /**
   * procura e retorna a rota que protege contra falha no link. Pode ser
   * retornado null se não houver uma rota disjunta ao link falho.
   * @param link Link
   * @return Route
   */
  private Route getRouteAR(Link link) {
    for (int i = 0; i < this.listLinkAR.size(); i++) {
      if (listLinkAR.get(i).getLink() == link) {
        return listLinkAR.get(i).getRestoration();
      }
    }
    //System.out.println("Classe ReqActRestoration, metodo getRouteAR ");
    //System.out.println("Não foi adicionada restauração para link "+link.getName());
    return null;
  }

  //------------------------------------------------------------------------------
  /**
   * sempre retorna false pois não implementa estratégia se sobrevivência.
   *
   * @param link Link
   * @param eMachine EventMachine
   * @return boolean
   */
  public boolean survive(Link link, EventMachine eMachine) {
    boolean flag = false;
    // libera os recursos da rota que falhou
    this.tearDownConnection();
    Route auxRoute = this.getRouteAR(link);
    if (auxRoute!=null){
      this.setRoute(auxRoute);
      
      boolean hasControlChannel = true;
      controlChannel = new int[1];
      flag = this.route.tryEstablish(this.waveList,this.mesh,this.pair.getCategory(),true);
    }else
      flag=false;

    if (flag == true) {
      //estabelecer a rota de restaruação
      this.establishConnection();
    }
    else {
      this.setRoute(null);
    }
    return flag;
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
