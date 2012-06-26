package measurement;

import java.util.Vector;
import routing.Route;

public class NodeMeasure {
  private String name;
  private int maximumWCUtilization;
  private double averageWCUtilization;
  private Vector<Route> routesCrossingThisNode;
  private int numMaxWC;

  public NodeMeasure(String name) {
    this.name = name;
    this.maximumWCUtilization = 0;
    this.numMaxWC = 0;
    this.routesCrossingThisNode = new Vector<Route> ();
  }

  /**
   * Retorna o nome do nó
   * @return String
   */
  public String getName() {
    return this.name;
  }

  //------------------------------------------------------------------------------
  /**
   * Atualiza o numero maximo de WCs utilizados
   * @param utilization int
   */
  public void updateMaximumWCUtilization(int utilization) {
    if (utilization > this.maximumWCUtilization) {
      this.maximumWCUtilization = utilization;
    }
  }

  //------------------------------------------------------------------------------
  /**
   * Retorna o numero maximo de WC utilizados
   * @return int
   */
  public int getMaximumWCUtilization() {
    return this.maximumWCUtilization;
  }

  //------------------------------------------------------------------------------
  /**
   * Configura a media de WC utilizados
   * @param averageWCUtilization double
   */
  public void setAverageWCUtilization(double averageWCUtilization) {
    this.averageWCUtilization = averageWCUtilization;
  }

  //------------------------------------------------------------------------------
  /**
   * Retorna o numero de WC utilizados
   * @return double
   */
  public double getAverageWCUtilization() {
    return this.averageWCUtilization;
  }

  //------------------------------------------------------------------------------
  /**
   * Retorna as rotas que passam por este nó (sendo intermediário)
   * @return Vector<Route>
   */
  public Vector<Route> getRoutesCrossingThisNode() {
    return this.routesCrossingThisNode;
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna o numero de WC
   * @return int
   */
  public int getNumMaxWC() {
    return numMaxWC;
  }

  //------------------------------------------------------------------------------
  /**
   * Configura as rotas que passam por este nó (sendo intermediário)
   * @param routesCrossingThisNode Vector
   */
  public void setRoutesCrossingThisNode(Vector<Route> routesCrossingThisNode) {
    this.routesCrossingThisNode = routesCrossingThisNode;
  }
  //------------------------------------------------------------------------------
  /**
   * Configura o numero maximo de WC possíveis
   * @param numMaxWC int
   */
  public void setNumMaxWC(int numMaxWC) {
    this.numMaxWC = numMaxWC;
  }

  //------------------------------------------------------------------------------
  /**
   * Adiciona uma rota na lista de rotas que passam por este nó (sendo intermediário)
   * @param r Route
   */
  public void addRoute(Route r) {
    this.routesCrossingThisNode.add(r);
  }

  //------------------------------------------------------------------------------
  public int getNumRoutesCrossing() {
    return this.routesCrossingThisNode.size();
  }
}
