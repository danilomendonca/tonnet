package traffic;

import java.util.Vector;
import simulator.RandGenerator;
import network.*;

public class PairNonUniformTraffic extends Traffic {

  private Vector<Pair> pairListByPrivilege;

  public PairNonUniformTraffic(Vector<Pair> pairList,Mesh mesh) {
    super(pairList, mesh);
    this.pairListByPrivilege=new Vector<Pair>();
    this.configureTrafficNotUniformPair();
  }

  /**
   * Retorna um par aleatório de acordo com o tipo de tráfego.
   * @return Pair
   */
  public Pair pairGenerator() {
    return this.trafficNotUniformPair();
  }

  //------------------------------------------------------------------------------
  /**
   * Gera um par aleatorio não uniforme com pesos diferentes para cada par
   * @return Pair
   */
  public Pair trafficNotUniformPair() {
    Pair par = null;
    int index = this.randomVar.randInt(0, this.pairListByPrivilege.size() - 1);
    par = this.pairListByPrivilege.get(index);
    /*System.out.println("par gerado : " + par.getSource().getName() + " -> " +
                       par.getDestination().getName());
     */
    return par;
  }
  //----------------------------------------------------------------------------
  /**
   * Configura o tráfego não uniforme
   * Cada PAR tem um privilégio diferente.
   */
  private void configureTrafficNotUniformPair() {
    if (this.pairList.get(0).getPrivilege() != 0) {
      for (int i = 0; i < this.pairList.size(); i++) {
        Pair aux = this.pairList.get(i);
        //adicionando j vezes na lista de pares por privilegio
        for (int j = 0; j < aux.getPrivilege(); j++) {
          this.pairListByPrivilege.add(aux);
        }
      }
    }
    else {
      this.pairListByPrivilege = this.pairList;
    }
  }

}
