package network;

public class Wc {
  private int id;
  /**
   * se state = false o wC está livre.
   */
  private boolean state=false;

  /**
   * constroi conversor com identificar x.
   * @param x int
   */
  public Wc(int x) {
    this.id=x;
  }

  /**
   * define este conversor como ocupado
   */
  public void use(){
    state=true;
  }
  /**
   * libera este conversor
   */
  public void liberate (){
    state=false;
  }
  /**
   * retorna true se o conversor estiver livre.
   * @return boolean
   */
  public boolean wcFree(){
    return !state;
  }
}
