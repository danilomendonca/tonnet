package request;

import java.util.Vector;
import network.*;
import routing.Route;
import simulator.*;

public abstract class RequestMother {
  protected Route route;
  protected Pair pair;
  protected Mesh mesh;
  /** de comprimentos de onda utilizados em todos os enlaces da rota.
   *Se não houver conversão de comprimento de onda o vetor vai armazenar
   *o mesmo valor para todos os enlaces.
   */
  protected int[] waveList;
  private int[] controlChannel;
  
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
  /**
   * define quais o(s) comprimento(s) utilizado para atender a conexão. A ocupação
   * destes recursos é feita pelo método useWavelength().
   * @param index int
   * @param wave int
   */
  public void setWaveList(int index, int wave) {
    this.waveList[index] = wave;
  }
  
   /**
     * @return the controlChannel
     */
    public int[] getControlChannel() {
        return controlChannel;
    }

    /**
     * @param controlChannel the controlChannel to set
     */
    public void setControlChannel(int[] controlChannel) {
        this.controlChannel = controlChannel;
    }

//------------------------------------------------------------------------------
  public boolean RWA() {

    return this.routing();

  }
  
  public boolean establish(boolean rwa){
    
    boolean establish = rwa;
    
    if (establish) {
      this.establishConnection();
    }
    return establish;
    
  }

  //------------------------------------------------------------------------------
  /**
   * agenda uma nova requisição do mesmo tipo
   * @param time double
   * @param arrive ArriveRequest
   */
  public void scheduleNewArrivedRequest(double time, ArriveRequest arrive) {
    double deltaTime = arrive.getMesh().getRandomVar().negexp(arrive.getArrivedRate());
    double finalizeTime = arrive.getMesh().getRandomVar().negexp(arrive.getHoldRate());
    Request rEndToEnd, first = null;

    /*Event e = new Event(r, arrive, time + delta);
    arrive.getEMachine().insert(e);*/

    Pair nextPair = arrive.getMesh().pairGenerator();
    Vector <Node> nosRota = this.mesh.getRoutingControl().getRoutes(nextPair).get(0).getNodeList();    
    rEndToEnd = (Request) this.getNewRequest(nextPair, arrive.getMesh());    
    //rEndToEnd.establish(rEndToEnd.RWA());
    
    int [] waveList = null;
    
    for (int i = 0; i < nosRota.size() - 1; i ++){

        Node noA = nosRota.get(i);
        Node noB = nosRota.get(i + 1);
        Event e;
        Pair pair = arrive.getMesh().searchPair(noA, noB);
        Route route = this.mesh.getRoutingControl().getRoutes(pair).get(0);
        Request r = (Request) this.getNewRequest(pair, arrive.getMesh());
        if(i == 0)
            first = r;
        else
            first.getRelatedRequests().add(r);
        
        //seta manualmente o comprimento de onda e a rota para evitar a chamada ao RWA
        r.setRoute(route);
        r.mesh.setActualLinklist(route.getLinkList());
        
        e = new Event(r, arrive, time + deltaTime + finalizeTime * (i));        
        finalizeTime = arrive.getMesh().getRandomVar().negexp(arrive.getHoldRate());
        e.setFinalizeTime(finalizeTime);
        
        if(i != 0){
            e.setGenerateNext(false);
        }
        
        arrive.getEMachine().insert(e);        
    }
    
    first.setEndToEndRequest(rEndToEnd);
  }

  //------------------------------------------------------------------------------
  /**
   *Define que os comprimentos de onda alocados ficam ocupados nos
   *enlaces da(s) rota(s) utilizada pela requisição.
   */
  protected abstract void establishConnection();

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
