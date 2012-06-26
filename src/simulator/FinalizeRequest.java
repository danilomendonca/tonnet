package simulator;


import request.RequestMother;
import network.*;

public class FinalizeRequest
    implements EventListener {

  private Mesh mesh;

  /**
   *Constroi um obj. FinalizeRequest
   * @param m Mesh
   */
  public FinalizeRequest(Mesh m) {
    this.mesh = m;
  }

//------------------------------------------------------------------------------
  /**
   *Esse método é executado em função
   *de um evento FinalizeRequest escutado.
   * @param e Event
   */
  public void execute(Event e) {
    RequestMother request=((RequestMother)e.getObject());
    if (request.getRoute()!=null){
      request.tearDownConnection();
      this.mesh.getConnectionControl().removeRequest( (RequestMother) e.getObject());
    }
    else{
      System.out.println("rota nula");
    }
  }

}

