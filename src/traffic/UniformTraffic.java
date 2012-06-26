package traffic;

import java.util.Vector;
import simulator.RandGenerator;
import network.*;

public class UniformTraffic extends Traffic {

  public UniformTraffic(Vector<Pair> pairList,Mesh mesh) {
    super(pairList, mesh);
  }

  /**
   * Retorna um par aleat�rio de acordo com o tipo de tr�fego.
   * @return Pair
   */
  public Pair pairGenerator() {
    return this.uniformPairGenerator();
  }

  //------------------------------------------------------------------------------
  /**
   * Gera um par aleatorio uniforme com privilegio iguais para cada par
   * @return Pair
   */
  private Pair uniformPairGenerator() {
    Pair par = null;
    int index = this.randomVar.randInt(0, this.pairList.size() - 1);
    par = this.pairList.get(index);
    /*System.out.println("par gerado : " + par.getSource().getName() + " -> " +
                       par.getDestination().getName());
     */
    return par;
  }

}
