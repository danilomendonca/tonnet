package traffic;

import java.util.Vector;
import network.*;

public class TrafficControl {
  private Traffic traffic;

  public TrafficControl(int trafficType, Vector<Pair> pairList,Mesh mesh) {

    switch (trafficType) {
      case 0: //uniforme
        this.traffic = new UniformTraffic(pairList, mesh);
        break;
      case 1: //não uniforme em relação ao nó
        this.traffic = new NodeNonUniformTraffic(pairList, mesh);
        break;
      case 2: //não uniforme em relação ao par
        this.traffic = new PairNonUniformTraffic(pairList, mesh);
        break;
    }
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna um par aleatório de acordo com o tipo de tráfego.
   * @return Pair
   */
  public Pair pairGenerator() {
    return traffic.pairGenerator();
  }
   /**
  * Retorna um par a partir da origem e destino.
  * @param source
  * @param destination
  * @return
  */
 public Pair searchPair(Node source, Node destination){
  return this.traffic.searchPair(source, destination);
 }
  //----------------------------------------------------------------------------
 /**
  * Retorna a lista de pares
  * @return Vector
  */
 public Vector<Pair> getPairList() {
   return this.traffic.getPairList();
 }



}
