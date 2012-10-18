package simulator;

import java.util.Vector;
import request.RequestMother;
import routing.Route;
import request.ReqDPPTwoStep;
import network.*;
import request.Request;

public class ArriveRequest
    implements EventListener {

  private Mesh mesh;
  private FinalizeRequest finalizeRequest;
  private int numMaxRequest;
  private double holdRate;
  private double arrivedRate;
  private EventMachine eMachine;
  private String wAAlgorithm;

  /**
   *Constroi um objeto ArriveRequest
   * @param m Mesh
   * @param eM EventMachine
   * @param numMaxRequest int
   * @param holdRate double
   * @param arrivedRate double
   * @param simulationType int
   * @param wAAlgorithm String
   */
  public ArriveRequest(Mesh m, EventMachine eM, int numMaxRequest,
                       double holdRate, double arrivedRate,
                       String wAAlgorithm) {
    this.mesh = m;
    this.eMachine = eM;
    this.numMaxRequest = numMaxRequest;
    this.holdRate = holdRate;
    this.arrivedRate = arrivedRate;
    this.finalizeRequest = new FinalizeRequest(this.mesh);
    this.wAAlgorithm = wAAlgorithm;
  }

//------------------------------------------------------------------------------
  public double getArrivedRate() {
    return this.arrivedRate;
  }

  //------------------------------------------------------------------------------
  public double getHoldRate() {
    return this.holdRate;
  }

//------------------------------------------------------------------------------
  public String getWAAlgorithm() {
    return this.wAAlgorithm;
  }

//------------------------------------------------------------------------------
  /**
   *Esse método é executado em função
   *de um evento ArrivedRequest escutado.
   * @param e Event
   */
  public void execute(Event e) {
    // incrementa o nº de requisições geradas
    this.mesh.getMeasurements().incNumGeneratedReq();
    //somatorio da utilização da rede.
    this.getMesh().getMeasurements().sumOfUtilization(this.mesh.calculateUtilization());
    //somatorio da utilização por comprimento de onda.
    this.getMesh().getMeasurements().sumOfWavelenghtUtilization(this.mesh.calculateWavelengthUtilization());
    //sommatorio da utilizao por enlace
    this.getMesh().getMeasurements().calcSumUtilizationPerLink();

    RequestMother request = (RequestMother) e.getObject();
    // incrementa o nº de vezes que o par foi gerados
    request.getPair().incNumGenerated();

    //verifica se é necessário agendar a geração de novas requisições
    if (this.mesh.getMeasurements().getNumGeneratedReq() < this.numMaxRequest && e.isGenerateNext())
      request.scheduleNewArrivedRequest(e.getTime(), this);
    
    boolean established = true;
    if (request instanceof Request && e.isGenerateNext()){
        Request endToEnd = ((Request)request).getEndToEndRequest();
        Request first = ((Request)request);
        if(endToEnd != null){
            established = endToEnd.RWA();        
            
            if(established){
                first.setWaveList(0, endToEnd.getWaveList()[0]);
                established = first.establish(established);      
            }else
                eMachine.remove(first);
            
            for(Request r : ((Request)request).getRelatedRequests()){
                if(established){
                    r.setWaveList(0, endToEnd.getWaveList()[0]);
                    established = r.establish(established) && established;
                }else
                    eMachine.remove(r);
            }
            
            
            
        }else
            established = false;
    }
    
    if (established) {
      this.mesh.getConnectionControl().addRequest(request);
      double finalizeTime = e.getTime() + e.getFinalizeTime();
      this.eMachine.insert(new Event(e.getObject(), this.finalizeRequest, finalizeTime));

      //soma ao tamanho de todas requisições atendidas
      this.mesh.getMeasurements().sumAllSizeOfPrimaryAcceptedReq(request.getRoute().
          getHops());
      //soma ao tamanho de todas requisições de backup atendidas
      if (request.getProtection()) {
        Route bckp = null;
        if (request instanceof ReqDPPTwoStep){
          bckp = ( (ReqDPPTwoStep) request).getRouteBackup();
        }
        if (bckp != null)
          this.mesh.getMeasurements().sumAllSizeOfBackupAcceptedReq(bckp.getHops());
      }

    }
    else {
      //inc bloqueio geral
      this.mesh.getMeasurements().incBlocked();
      //inc bloqueio do par
      request.getPair().incNumBlocked();
      //soma ao tamanho de todas requisições bloqueidas
      if (request.getRoute()!=null)
        this.mesh.getMeasurements().sumAllSizeOfPrimaryBloqckedReq(request.getRoute().
            getHops());
      //soma ao tamanho de todas requisições de backup bloqueidas
      if (request.getProtection()) {
        Route bckp = null;
        if (request instanceof ReqDPPTwoStep){
          bckp = ( (ReqDPPTwoStep) request).getRouteBackup();
        }
        if (bckp != null)
          this.mesh.getMeasurements().sumAllSizeOfBackupBloqckedReq(bckp.
              getHops());
      }
    }
    this.mesh.getMeasurements().verifyMaximumWCUtilization(this.mesh.getNodeList());
  }

  /**
   * getMesh
   *
   * @return Object
   */
  public Mesh getMesh() {
    return mesh;
  }

  public void setNumMaxRequest(int num){
      this.numMaxRequest = num;
  }

  public int getNumMaxRequest(){
      return this.numMaxRequest;
  }
  

  /**
   * getEMachine
   *
   * @return Object
   */
  public EventMachine getEMachine() {
    return this.eMachine;
  }
}

