package measurement;

import java.util.Vector;
import routing.*;

public class LinkMeasure {

  private String name;
  private double numberFailure;
  private double utilization;
  private Vector<Route> routesWithThisLink;

  public LinkMeasure(String name, double numberFailure, double utilization) {
    this.name = name;
    this.numberFailure = numberFailure;
    this.utilization = utilization;
    this.routesWithThisLink = new Vector<Route>();
  }

  public LinkMeasure() {
  }

//------------------------------------------------------------------------------
  /**
   * retorna o nome deste link
   * @return String
   */
  public String getName() {
    return this.name;
  }

//------------------------------------------------------------------------------
  /**
   * retorna o numero de falhas geradas neste link
   * @return double
   */
  public double getNumberFailure() {
    return this.numberFailure;
  }

//------------------------------------------------------------------------------
  /**
   * retorna a utilização deste link
   * @return double
   */
  public double getUtilization() {
    return utilization;
  }
  //------------------------------------------------------------------------------
    /**
     * retorna as rotas que utilizam este link
     * @return Vector de rotas
     */
    public Vector getRoutes() {
      return this.routesWithThisLink;
  }
  //------------------------------------------------------------------------------
  /**
   * Adiciona uma rota na lista de rotas que utilizam este link
   * @param r Route
   */
  public void addRoute(Route r) {
    this.routesWithThisLink.add(r);
}

//------------------------------------------------------------------------------
  /**
   * configura a utilização deste link
   * @param utilization double
   */
  public void setUtilization(double utilization) {
    this.utilization = utilization;
  }

//------------------------------------------------------------------------------
  /**
   * configura o numero de falhas deste link
   * @param numberFailure double
   */
  public void setFaiure(double numberFailure) {
    this.numberFailure = numberFailure;
  }
;
}
