package simulator;

import request.RequestMother;
import routing.Route;
import request.ReqDPPTwoStep;
import network.*;

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
  public String getWAAlgorithm() {
    return this.wAAlgorithm;
  }

//------------------------------------------------------------------------------
  /**
   *Esse m�todo � executado em fun��o
   *de um evento ArrivedRequest escutado.
   * @param e Event
   */
  public void execute(Event e) {
    // incrementa o n� de requisi��es geradas
    this.mesh.getMeasurements().incNumGeneratedReq();
    //somatorio da utiliza��o da rede.
    this.getMesh().getMeasurements().sumOfUtilization(this.mesh.calculateUtilization());
    //somatorio da utiliza��o por comprimento de onda.
    this.getMesh().getMeasurements().sumOfWavelenghtUtilization(this.mesh.calculateWavelengthUtilization());
    //sommatorio da utilizao por enlace
    this.getMesh().getMeasurements().calcSumUtilizationPerLink();

    RequestMother request = (RequestMother) e.getObject();
    // incrementa o n� de vezes que o par foi gerados
    request.getPair().incNumGenerated();
    //verifica se � necess�rio agendar a gera��o de novas requisi��es
    if (this.mesh.getMeasurements().getNumGeneratedReq() <
        this.numMaxRequest)
      request.scheduleNewArrivedRequest(e.getTime(),this);

    if (request.RWA()) {
      this.mesh.getConnectionControl().addRequest(request);
      double finalizeTime = e.getTime() +
          this.mesh.getRandomVar().negexp(this.holdRate);
      this.eMachine.insert(new Event(e.getObject(), this.finalizeRequest,
                                     finalizeTime));

      //soma ao tamanho de todas requisi��es atendidas
      this.mesh.getMeasurements().sumAllSizeOfPrimaryAcceptedReq(request.getRoute().
          getHops());
      //soma ao tamanho de todas requisi��es de backup atendidas
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
      //soma ao tamanho de todas requisi��es bloqueidas
      if (request.getRoute()!=null)
        this.mesh.getMeasurements().sumAllSizeOfPrimaryBloqckedReq(request.getRoute().
            getHops());
      //soma ao tamanho de todas requisi��es de backup bloqueidas
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

  /**
   * getEMachine
   *
   * @return Object
   */
  public EventMachine getEMachine() {
    return this.eMachine;
  }
}

