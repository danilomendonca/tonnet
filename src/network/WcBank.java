package network;

import java.util.Vector;

public class WcBank {
  private Vector <Wc> wcList ;
  public WcBank(int numWcs) {
    wcList = new Vector();
    for (int i = 1; i <= numWcs; i++) {
      wcList.add(new Wc(i));
    }
  }
  /**
   * retorna numero de conversores
   * @return int
   */
  public int getNumWcs(){
    return this.wcList.size();
  }
  /**
   * retorna true se houver um WC livre.
   * @return boolean
   */
  public boolean wcFree(){
    for (int i = 0; i < wcList.size(); i++) {
      if (wcList.get(i).wcFree()) {
        return true;
      }
    }
    return false;
  }

  /**
   * retorna true se a conversão foi realizada com sucesso
   * @return boolean
   */
  public boolean useWC(){
    for (int i = 0; i < wcList.size(); i++) {
      if (wcList.get(i).wcFree()) {
        wcList.get(i).use();
        return true;
      }
    }
    return false;
  }
  /**
   * retorna true se o WC foi liberado com sucesso
   * @return boolean
   */
  public boolean liberateWC(){
      for (int i = 0; i < wcList.size(); i++) {
        if (!wcList.get(i).wcFree()) {
          wcList.get(i).liberate();
          return true;
        }
      }
      return false;
  }

  public int getNumberUseWc(){
    int number=0;
    for (int i = 0; i < this.wcList.size(); i++) {
      if (!this.wcList.get(i).wcFree())
        number++;
    }
    return number;
  }
}
