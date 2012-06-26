package simulator;

import java.util.Vector;

import measurement.*;
import network.*;

public class OccurFailure implements EventListener {
  private Mesh mesh;
  private int numMaxFailure;
  private int numMaxRequest;
  private double fixLinkRate;
  private double occurRate;
  private EventMachine eMachine;
  private FixFailure fixFailure;
  private int failureType;

  public OccurFailure(Mesh m, int numMaxRequest, EventMachine eM, int numMaxFault,
                       double fixLinkRate, double occurRate) {
    this.mesh=m;
    this.numMaxRequest = numMaxRequest;
    this.eMachine=eM;
    numMaxFailure=numMaxFault;
    this.fixLinkRate=fixLinkRate;
    this.occurRate=occurRate;
    this.failureType=failureType;
    this.fixFailure=new FixFailure(this.mesh);
  }

  public double getoccurRate(){
    return occurRate;
  }

  public void execute(Event e){

    // teste #######################################
    //System.out.println("falha = "+this.mesh.getMeasurements().getNumGeneratedFailure());
    if (this.mesh.falha>0){
      this.mesh.countMoreOneFailure++;
    }
    else{
      this.mesh.falha++;
    }
    // teste fim ##################################

      // cria a falha
      Failure failure = new Failure(mesh.generateLinkFailure());

      // adiciona o enlace falho ao evento de falha.
      e.setObject(failure);

      // adiciona o enlace na lista de enlaces falhos atualmente.
      mesh.addLinkFaild(failure.getListLink());

      //incrementa o num. de falhas geradas
      this.mesh.getMeasurements().incNumGeneratedFailure();

      // configura o enlace como falho
      ( (Failure) e.getObject()).fail();

      // iniciar o mecanismo de detecção de falha e sobrevivencia
      this.mesh.getConnectionControl().failureDetect(failure.getListLink(),this.eMachine);

      //agenda evento para reparação dos enlaces.
      this.scheduleFixFailureEvent(e);

    //}

    //agenda nova falha
    if (this.mesh.getMeasurements().getNumGeneratedReq() <
        this.numMaxRequest - 1) {
      if (this.mesh.getMeasurements().getNumGeneratedFailure() <
          this.numMaxFailure - 1)
        this.scheduleFailureEvent(e.getTime());
    }


  }

  /**
   * retorna um enlace sorteado aleatoriamente dentre todos os enlaces possíveis.
   * Assume que o enlace é bidirecional. Isto é, o método retorna o enlace de
   * ida e o enlace de volta.
   * @return Link
   */
  private Vector<Link> getRandomLink(){
      Vector<Link> listLinks = new Vector<Link>();
      int x=mesh.getRandomVar().randInt(0, mesh.getLinkList().size() - 1);
      Link auxLink=mesh.getLinkList().get(x);
      listLinks.add(auxLink);
      listLinks.add(mesh.getLink(auxLink.getSource().getName(),auxLink.getDestination().getName()));
      return listLinks;

  }

  /**
   * agenda um evento de falha de um único enlace
   * @param time double
   */
  public void scheduleFailureEvent(double time){
   double timeFailure = time+mesh.getRandomVar().negexp(this.occurRate);
       Event event = new Event(null, this, timeFailure);
       this.eMachine.insert(event);
 }

 /**
  * agenda um evento de reparação de falha
  * @param e Event
  */
 public void scheduleFixFailureEvent(Event e){
   double timeFailure = e.getTime()+mesh.getRandomVar().negexp(this.fixLinkRate);
       Event event = new Event((Failure)e.getObject(), this.fixFailure, timeFailure);
       this.eMachine.insert(event);
 }


}
