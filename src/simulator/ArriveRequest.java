package simulator;

import java.util.Arrays;
import java.util.Vector;
import request.RequestMother;
import routing.Route;
import request.ReqDPPTwoStep;
import network.*;
import request.Request;
import routing.RoutingControl;
import traffic.FuzzyClassification;

/**
 * Classe do tipo evento de requisição
 * 
 */
public class ArriveRequest
    implements EventListener {

  private Mesh mesh;
  private FinalizeRequest finalizeRequest;
  private int numMaxRequest;
  private double holdRate;
  private double arrivedRate;
  private EventMachine eMachine;
  private String wAAlgorithm;
  private ControlRequest controlRequest;

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
       
    RequestMother request = (RequestMother) e.getObject();
    if(!e.isBurstPackage()){
        // incrementa o nº de requisições geradas
        this.mesh.getMeasurements().incNumGeneratedReq();
        //somatorio da utilização da rede.
        this.getMesh().getMeasurements().sumOfUtilization(this.mesh.calculateUtilization());
        //somatorio da utilização por comprimento de onda.
        this.getMesh().getMeasurements().sumOfWavelenghtUtilization(this.mesh.calculateWavelengthUtilization());
        //sommatorio da utilizao por enlace
        this.getMesh().getMeasurements().calcSumUtilizationPerLink();
        
        // incrementa o nº de vezes que o par foi gerados
        request.getPair().incNumGenerated();
    }
    
    //verifica se é necessário agendar a geração de novas requisições
    if (this.mesh.getMeasurements().getNumGeneratedReq() < this.numMaxRequest && !e.isBurstPackage()){
        //TODO: calcular realLambda para tráfegos não uniformes
        //classifica tráfego para utilizar rajada ou circuito
        double realLambda = getArrivedRate()/this.getMesh().getLinkList().size();
        if(FuzzyClassification.classifyTraffic(realLambda, 0.5, getMesh().getRandomVar().negexp(realLambda)) == RoutingControl.BURST)
          request.scheduleNewArrivedRequest(e.getTime(),this.getControlRequest());
        else
          request.scheduleNewArrivedRequest(e.getTime(),this);
    }

    if (e.isBurstPackage() || request.establish(request.RWA())) {
      this.mesh.getConnectionControl().addRequest(request);
      
      
      double finalizeTime;
      //rajadas já são contabilizadas no evento de controle
      if(!e.isBurstPackage()){
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
        
        finalizeTime = e.getTime() + this.mesh.getRandomVar().negexp(this.holdRate);             
      }else
        finalizeTime = e.getTime() + this.mesh.getRandomVar().negexp(this.holdRate)/100; //TODO: Qual valor???           
      
        this.eMachine.insert(new Event(e.getObject(), this.finalizeRequest,
                                     finalizeTime));
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

  public ControlRequest getControlRequest() {
      return controlRequest;
  }

  public void setControlRequest(ControlRequest controlRequest) {
      this.controlRequest = controlRequest;
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

