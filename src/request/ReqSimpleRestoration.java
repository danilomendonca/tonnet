package request;

import network.*;
import routing.Route;
import simulator.EventMachine;

public class ReqSimpleRestoration
    extends RequestMother {

  public ReqSimpleRestoration(Pair p, Mesh mesh) {
    super(p, mesh);
  }

  //------------------------------------------------------------------------------
  /**
   * Roteamento fixo
   * @return boolean
   */
  protected boolean routing() {
    Route routeAux;
    routeAux = this.mesh.getRoutingControl().getRoutes(this.pair).get(0);
    if (routeAux != null) {
      this.setRoute(routeAux);
      return this.route.tryEstablish(this.waveList, this.mesh,
                                     this.pair.getCategory(), true);
    }
    return false;
  }

  //------------------------------------------------------------------------------
  /**
   *Define que os comprimentos de onda alocados ficam livres nos
   *enlaces da rota utilizada pela requisi��o.
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
   *enlaces da rota utilizada pela requisi��o.
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
  /**
   * retorna uma nova requisi��o do mesmo tipo
   * @param p Pair
   * @param m Mesh
   * @return RequestMother
   */
  public RequestMother getNewRequest(Pair p, Mesh m) {
    return new ReqSimpleRestoration(p, m);
  }

  //------------------------------------------------------------------------------
  /**
   * Retorna false se n�o conseguir sobreviver a falha do link. As rotas das
   * requisi��es n�o recuperadas s�o definidas com null.
   *
   * @param link Link
   * @param eMachine EventMachine
   * @return boolean
   */
  public boolean survive(Link link, EventMachine eMachine) {
    // libera os recursos da rota que falhou
    this.tearDownConnection();
    Route restoration = this.mesh.getRoutingControl().getRoutes(this.pair).get(1);
    boolean flag = false;
    if (restoration != null) {
      this.setRoute(restoration);
      flag = this.route.tryEstablish(this.waveList, this.mesh,
                                     this.pair.getCategory(), true);
    }
    if (flag == true) {
      //estabelecer a rota de restaura��o
      this.establishConnection();
      //faz somat�rio dos tempos de restaura��o
      //this.mesh.getMeasurements().sumRestorationTime(calculateRestorationTime(link, restoration));
    }
    else {
      this.setRoute(null); //PORQUE PRECISA, se irei remover o evento de finalizar???
      eMachine.remove(this);
    }
    return flag;
  }

  //------------------------------------------------------------------------------
  /**
   *  A = TEMPO PARA DETEC��O DA FALHA
   *  X = N� DE SALTOS DO NO DE DESTINO DO ENLACE FALHO AT� O N� DE ORIGEM DA
   *      REQUISI��O
   *  Y = N� DE SALTOS DA ROTA DE BACKUP
   *  P = ATRASO DE PROPAGA��O ENLACE
   *  B = TEMPO PARA PROCESSAMENTO DA MENSAGEM E TRANSMISS�O EM CADA N�
   *  C = TEMPO PARA PROCESSAR A MENSAGEM DE CONTROLE E CONFIGURAR O OXC
   * @param linkAB Link
   * @param restorationRoute Route
   * @return double
   */
  public double calculateRestorationTime(Link linkAB, Route restorationRoute) {

    double A = 0, P = 0, B = 0, C = 0, restorationTime = 0;
    int X, Y;
    // tempo para detectar a falha.
    restorationTime = restorationTime + A;
    // numero de enlaces do n� B (no de destino do enlace falho)
    // para o n� de destino da rota prim�ria.
    this.route.hopNumFromNodeBToNodeX(linkAB);

    return 0;
  }

  //------------------------------------------------------------------------------
  public double calculateConnectionSetup() {
    return 0;
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
