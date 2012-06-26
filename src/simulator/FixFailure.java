package simulator;

import network.*;

public class FixFailure implements EventListener {

  private Mesh mesh;

  public FixFailure(Mesh m) {
    this.mesh=m;
  }

  public void execute(Event e) {
   Failure  failure = (Failure)e.getObject();

    // teste ########################################
    //System.out.print("reparou links:");
    //failure.printLinks();


    this.mesh.falha--;
    // teste fim  ###################################

    // remove o enlace da lista de enlaces falhos atualmente.
    mesh.removeLinkFaild(failure.getListLink());
    //configura os enlaces como em perfeito funcionamento.
    failure.fixFailure();

  }

}
