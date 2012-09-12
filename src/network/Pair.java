package network;

public class Pair {

  private Node source;
  private Node destination;
  private double numGenerated;
  private double numBlocked;
  private int id;
  private int category;
  private int privilege;

  /**
   * Cria instancia de Pair
   * @param s Node
   * @param d Node
   */
  public Pair(Node s, Node d) {
    this.source = s;
    this.destination = d;
    this.numGenerated = 0;
    this.numBlocked = 0;
  }

  /**
   * configura id
   * @param id int
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * retorna o id
   * @return int
   */
  public int getId() {
    return this.id;
  }

  /**
   * configura a categoria
   * @param category int
   */
  public void setCategory(int category) {
    this.category = category;
  }

  public void setPrivilege(int privilege) {
    this.privilege = privilege;
  }

  /**
   * retorna o source
   * @return Node
   */
  public Node getSource() {
    return this.source;
  }

  /**
   * retorna o numero de requisições geradas por este par
   * @return double
   */
  public double getNumGenerated() {
    return this.numGenerated;
  }

  /**
   * retorna o numero de bloqueios do par
   * @return double
   */
  public double getNumBlocked() {
    return this.numBlocked;
  }

  /**
   * retorna o destino
   * @return Node
   */
  public Node getDestination() {
    return this.destination;
  }

  /**
   * retorna a categoria do par
   * @return int
   */
  public int getCategory() {
    return this.category;
  }

  public int getPrivilege() {
    return this.privilege;
  }

  /**
   * incrementa o numero de requisições geradas por este par
   */
  public void incNumGenerated() {
    this.numGenerated++;
  }

  /**
   * incrementa o bloqueio dete par
   */
  public void incNumBlocked() {
    this.numBlocked++;
  }

  /**
   * exibe o par na console
   * @return String
   */
  public String printPair() {
    return "(" + this.source.getName() + "," + this.destination.getName() + ")";
  }

  /**
   * exibe o bloqueio do par na console
   * @return String
   */
  public String getBlockPair() {
    String s = "";
    s += ("Pb pair (" + this.source.getName() + "," +
          this.destination.getName() +
          ") =" + this.numBlocked / this.numGenerated);
    // s+=("numero de vezes que o par foi gerado " + this.numGenerated);
    return s;
  }

  /**
   * getName
   * @return Object
   */
  public String getName() {
    return ("(" + this.source.getName() + "," + this.destination.getName() +
            ")");
  }

  /**
   * getPbPair
   * @return double
   */
  public double getPbPair() {
    if (this.getNumGenerated() == 0) {
      return 0;
    }
    return (this.getNumBlocked() / this.getNumGenerated());
  }

}
