package traffic;

import java.util.Vector;
import simulator.RandGenerator;
import network.*;

public class NodeNonUniformTraffic extends Traffic {

  private Vector<Node> nodeListWithPrivilege;

  public NodeNonUniformTraffic(Vector<Pair> pairList,Mesh mesh) {
    super(pairList, mesh);
    this.nodeListWithPrivilege = new Vector<Node> ();
    this.configureTrafficNotUniformNode();
  }

  /**
   * Retorna um par aleatório de acordo com o tipo de tráfego.
   * @return Pair
   */
  public Pair pairGenerator() {
    return this.trafficNotUniformNode();
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna um par aleatório com pesos diferenciados para cada nó
   * @return Pair
   */
  private Pair trafficNotUniformNode() {
    Node s, d;
    do {
      int index = this.randomVar.randInt(0,
                                         this.nodeListWithPrivilege.size() - 1);
      s = this.nodeListWithPrivilege.get(index);

      index = this.randomVar.randInt(0, this.nodeListWithPrivilege.size() - 1);
      d = this.nodeListWithPrivilege.get(index);
    }
    while (s == d);
    return this.searchPair(s, d);

  }

  //----------------------------------------------------------------------------
  /**
   * Configura o tráfego não uniforme
   * Cada NÓ tem um privilégio diferente.
   */
  private void configureTrafficNotUniformNode() {
    for (int i = 0; i < this.mesh.getNodeList().size(); i++) {
      Node aux = this.mesh.getNodeList().get(i);
      for (int j = 0; j < aux.getPrivilege(); j++) {
        this.nodeListWithPrivilege.add(aux);
      }
    }
  }
}
