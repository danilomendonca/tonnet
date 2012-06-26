package measurement;

public class PairMeasure {
  private String name;
  private double numberReq;
  private double pBblocking;
  private double size;
  private int category;

  public PairMeasure(String name, double numberReq, double pBblocking, double size, int category) {
    this.name = name;
    this.numberReq = numberReq;
    this.pBblocking = pBblocking;
    this.size = size;
    this.category = category;
  }

  public PairMeasure() {

  }

//------------------------------------------------------------------------------
  /**
   * retorna o nome do par
   * @return String
   */
  public String getName() {
    return this.name;
  }

//------------------------------------------------------------------------------
  /**
   * Retorna o numero de vezes que este par foi gerado
   * @return double
   */
  public double getNumberReq() {
    return this.numberReq;
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna a categoria do par
   * @return int
   */
  public int getCategory() {
    return this.category;
  }
//------------------------------------------------------------------------------
  /**
   * retorna a probabilidade de bloqueio deste par
   * @return double
   */
  public double getPbBlocking() {
    return this.pBblocking;
  }
  //------------------------------------------------------------------------------
  /**
   * retorna o tamanho das requisições deste par
   * @return double
   */
  public double getSize() {
    return this.size;
  }
//------------------------------------------------------------------------------
  /**
   * configura pb de bloqueio deste par
   * @param pBblocking double
   */
  public void setPbBlocking(double pBblocking) {
    this.pBblocking = pBblocking;
  }

//------------------------------------------------------------------------------
  /**
   * configura o numero de requisições des par
   * @param numberReq double
   */
  public void setNumberReq(double numberReq) {
    this.numberReq = numberReq;
  }
  //------------------------------------------------------------------------------
  /**
   * configura o tamanho das requisições deste par
   * @param size double
   */
  public void setSize(double size){
    this.size=size;
  }
}
