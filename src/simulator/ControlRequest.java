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
public class ControlRequest
    implements EventListener {

  private Mesh mesh;
  private FinalizeRequest finalizeRequest;
  private int numMaxRequest;
  private double holdRate;
  private double arrivedRate;
  private EventMachine eMachine;
  private String wAAlgorithm;
  private ArriveRequest arriveRequest;

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
  public ControlRequest(Mesh m, EventMachine eM, int numMaxRequest,
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
    if (this.mesh.getMeasurements().getNumGeneratedReq() < this.numMaxRequest){
      //TODO: calcular realLambda para tráfegos não uniformes
      //classifica tráfego para utilizar rajada ou circuito
      double realLambda = getArrivedRate()/this.getMesh().getLinkList().size();
      float hurstMin = this.mesh.getMeasurements().getHurstMin();
      float hurstMax = this.mesh.getMeasurements().getHurstMax();
      float hurst = this.mesh.getRandomVar().randInt((int)(hurstMin * 100), (int)(hurstMax * 100)) / 100;
      if(FuzzyClassification.classifyTraffic(realLambda, hurst, getMesh().getRandomVar().negexp(realLambda)) == RoutingControl.BURST)
        request.scheduleNewArrivedRequest(e.getTime(),this);
      else
        request.scheduleNewArrivedRequest(e.getTime(),this.getArriveRequest());
    }
    boolean established = true;
    boolean rwa = true;
        
    //Bloco para tratamento do pacote de controle num roteamento por pacotes sem conversão
    if (request instanceof Request && !e.isBurstPackage()){
        Request controlChannel = ((Request)request);
        if(controlChannel != null){
            rwa = controlChannel.RWA();   
            
            int waveLength = controlChannel.getWaveList()[0];
                   
            //Efetua conexão do canal de controle
            //Recupera comprimento de onda reservado ao canal de controle
            //int controlLambda = controlChannel.getRoute().getFirstLinkNumWave(); //seta o último comprimento de onda disponível  
            //for(int i = 0; i < controlChannel.getWaveList().length; i++)
            //    controlChannel.setWaveList(i, controlLambda);      
            //Estabelece conexão
            //established = controlChannel.establish(rwa);
                      
            //Caso conexão do pct cnt tenha sido feita
            //if(established){
                //double finalizeControlTime = e.getTime() + getMesh().getRandomVar().negexp(holdRate) / 1000000; //TODO: verificar proporção do hold time de controle
                //this.eMachine.insert(new Event(controlChannel, this.finalizeRequest, finalizeControlTime));
                                
                //Realiza conexão de todos os pacotes intermediários
                for(Request r : controlChannel.getRelatedRequests()){
                    if(established){//Caso a conexão do atual evento intermediário tenha sido feita
                        r.setWaveList(0, waveLength);
                        established = r.establish(established) && established;
                     }else{//Se não, remove este e os próximos eventos intermediários
                        eMachine.remove(r);                            
                     }
                }
            //}else{//Caso contrário, remove eventos intermediários sucessivos da ME
                //eMachine.remove(first);                        
            //    for(Request r : ((Request)request).getRelatedRequests())
            //        eMachine.remove(r);                
            //}    
            
        }else
            established = false;
    }
    
    if (established) {
      this.mesh.getConnectionControl().addRequest(request);
      //double finalizeTime = e.getTime() + e.getFinalizeTime();
      //this.eMachine.insert(new Event(e.getObject(), this.finalizeRequest, finalizeTime));

      //soma ao tamanho de todas requisições atendidas      
      this.mesh.getMeasurements().sumAllSizeOfPrimaryAcceptedReq(request.getRoute().getHops());
      
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

  public ArriveRequest getArriveRequest() {
      return arriveRequest;
  }

  public void setArriveRequest(ArriveRequest arriveRequest) {
      this.arriveRequest = arriveRequest;
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

