package traffic;

import java.util.Vector;
import simulator.RandGenerator;
import network.*;

public abstract class Traffic {
  protected RandGenerator randomVar;
  protected Mesh mesh;
  protected Vector<Pair> pairList;

  public Traffic(Vector<Pair> pairList,Mesh mesh) {
    this.pairList = pairList;
    this.randomVar = new RandGenerator();
    this.mesh = mesh;
  }

  /**
   * Retorna um par aleatório de acordo com o tipo de tráfego.
   * @return Pair
   */
  public abstract Pair pairGenerator();

  //----------------------------------------------------------------------------
  /**
   * Localiza um par em função dos nos origem-destino.
   * @param source Node
   * @param destination Node
   * @return Pair
   */
  public Pair searchPair(Node source, Node destination) {
    for (int i = 0; i < this.pairList.size(); i++) {
      if ( (this.pairList.get(i).getSource() == source) &&
          (this.pairList.get(i).getDestination() == destination)) {
        return this.pairList.get(i);
      }
    }
    return null;
  }

  //------------------------------------------------------------------------------
  public String printBlockperPair() {
    String s = "";
    for (int i = 0; i < this.pairList.size(); i++) {
      s += ("---------------------------------------------------------\n");
      s += pairList.get(i).getBlockPair() + "\n";
    }
    return s;
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna a lista de pares
   * @return Vector
   */
  public Vector<Pair> getPairList() {
    return this.pairList;
  }

}
