package request;

import java.util.Vector;
import network.*;
import routing.Route;
import simulator.*;

public abstract class RequestMother {
  protected Route route;
  protected Pair pair;
  protected Mesh mesh;
  private boolean storedRequest = false;
  
  /** de comprimentos de onda utilizados em todos os enlaces da rota.
   *Se não houver conversão de comprimento de onda o vetor vai armazenar
   *o mesmo valor para todos os enlaces.
   */
  protected int[] waveList;
  protected int[] controlChannel;
  
  protected boolean protection;

  public RequestMother(Pair p, Mesh mesh) {
    this.pair = p;
    this.mesh = mesh;
    this.protection = false;
  }

  //------------------------------------------------------------------------------
  public Pair getPair() {
    return this.pair;
  }

//------------------------------------------------------------------------------
  public Node getSource() {
    return this.pair.getSource();
  }

//------------------------------------------------------------------------------
  public Node getDestination() {
    return this.pair.getDestination();
  }

//------------------------------------------------------------------------------
  public Route getRoute() {
    return this.route;
  }

//------------------------------------------------------------------------------
  public void setRoute(Route route) {
    this.route = route;
    if (this.route != null) {
      this.waveList = new int[this.route.getHops()];
    }
  }

  //------------------------------------------------------------------------------
  public int[] getWaveList() {
    return this.waveList;
  }
  
  //------------------------------------------------------------------------------
  public boolean isStoredRequest() {
      return storedRequest;
  }

  public void setStoredRequest(boolean storedRequest) {
      this.storedRequest = storedRequest;
  }  

  //------------------------------------------------------------------------------
  /**
   * define quais o(s) comprimento(s) utilizado para atender a conexão. A ocupação
   * destes recursos é feita pelo método useWavelength().
   * @param index int
   * @param wave int
   */
  public void setWaveList(int index, int wave) {
    this.waveList[index] = wave;
  }
 
//------------------------------------------------------------------------------
  public boolean RWA() {

    return this.routing();

  }
  
  public boolean establish(boolean rwa){
    
    boolean establish = rwa;
    
    if (establish) {
        return this.establishConnection();
    }else
        return false;
    
  }

  //------------------------------------------------------------------------------
  /**
   * agenda uma nova requisição do mesmo tipo
   * usado no roteamento por circuitos
   * @param time double
   * @param arrive ArriveRequest
   */
   public void scheduleNewArrivedRequest(double time, ArriveRequest arrive) {
    double deltaTime = arrive.getMesh().getRandomVar().negexp(arrive.
        getArrivedRate());
    RequestMother r = null;

    r = this.getNewRequest(arrive.getMesh().pairGenerator(), arrive.getMesh());

    Event e = new Event(r, arrive, deltaTime + time);
    e.setBurstPackage(false);

    arrive.getEMachine().insert(e);

  }

  //------------------------------------------------------------------------------
  /**
   * agenda uma nova requisição composta por um conjunto de subrequisições 
   * usado no roteamento por rajadas
   * @param time double
   * @param arrive ArriveRequest
   */
  public void scheduleNewArrivedRequest(double time, ControlRequest control) {
    
    
    double deltaTime = control.getMesh().getRandomVar().negexp(control.getArrivedRate());
    double finalizeTime = control.getMesh().getRandomVar().negexp(control.getHoldRate());
    Request rEndToEnd, first = null;

    /*Event e = new Event(r, arrive, time + delta);
    arrive.getEMachine().insert(e);*/

    Pair nextPair = control.getMesh().pairGenerator();
    
    Vector <Node> nosRota = this.mesh.getRoutingControl().getRoutes(nextPair).get(0).getNodeList();    
    rEndToEnd = (Request) this.getNewRequest(nextPair, control.getMesh());    

    Event e;
    e = new Event(rEndToEnd, control, time + deltaTime);   
    control.getEMachine().insert(e);    

    ArriveRequest arrive = control.getArriveRequest();

    for (int i = 0; i < nosRota.size() - 1; i ++){

        Node noA = nosRota.get(i);
        Node noB = nosRota.get(i + 1);

        Pair pair = arrive.getMesh().searchPair(noA, noB);
        Route route = this.mesh.getRoutingControl().getRoutes(pair).get(0);
        Request r = (Request) this.getNewRequest(pair, arrive.getMesh());
        rEndToEnd.getRelatedRequests().add(r);

        r.setRoute(route);
        r.mesh.setActualLinklist(route.getLinkList());

        e = new Event(r, arrive, time + deltaTime + finalizeTime * (i + 1));                 
        e.setBurstPackage(true);
        finalizeTime = arrive.getMesh().getRandomVar().negexp(arrive.getHoldRate()) / (nosRota.size() - 1);//número de saltos
        e.setFinalizeTime(finalizeTime);

        arrive.getEMachine().insert(e);        
    }
  }
  
  //------------------------------------------------------------------------------
  /**
   *Define que os comprimentos de onda alocados ficam ocupados nos
   *enlaces da(s) rota(s) utilizada pela requisição.
   */
  protected abstract boolean establishConnection();

  /**
   *Define que os comprimentos de onda alocados ficam livres nos
   *enlaces da(s) rota(s) utilizada pela requisição.
   */
  public abstract void tearDownConnection();

  /**
   * Verifica se ha possibilidade de atender a requisicao
   * @return boolean
   */
  protected abstract boolean routing();

  /**
   * Retorna uma nova requisição do mesmo tipo
   * @param p Pair
   * @param m Mesh
   * @return RequestMother
   */
  protected abstract RequestMother getNewRequest(Pair p, Mesh m);

  /**
   * Implementa estratégia se sobrevivência.
   * @param link Link
   * @param eMachine EventMachine
   * @return boolean
   */
  public abstract boolean survive(Link link, EventMachine eMachine);

  /**
   * Retorna true se a(as) rota(as) utilizada(s) for(am) afetada(s) pela falha do link.|
   * @param link Link
   * @return boolean
   */
  public abstract boolean requestAffected(Link link);

  //------------------------------------------------------------------------------
  public void printListWave() {
    System.out.print("Rota: ");
    this.route.printRoute();
    System.out.print("WaveList: ");
    for (int i = 0; i < this.waveList.length; i++) {
      System.out.print(this.waveList[i] + ",");
    }
    System.out.println("");
    System.out.println("------------------------------------------------");
  }

  /**
   * Retorna true caso implementa proteção (rota primaria e rota backup)
   * @return boolean
   */
  public boolean getProtection() {
    return this.protection;
  }

   
}
