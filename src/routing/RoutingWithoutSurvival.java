package routing;

import java.util.Vector;
import network.Link;
import network.Node;

public abstract class RoutingWithoutSurvival
    extends RoutingMother {
  /**
   * Lista com rota para cada par.
   */
  protected Vector<Route> routeList;

  public RoutingWithoutSurvival(Vector<Node> nodeList, Vector<Link> linkList) {
    super(nodeList, linkList);

    this.routeList=new Vector<Route>();
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna a rota de todos os pares.
   * @return Vector
   */
  public Vector<Route> getRoutesForAllPairs(){
    return this.routeList;
  }

}
