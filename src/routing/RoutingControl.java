package routing;

import network.Pair;
import network.Link;
import java.util.Vector;
import network.Node;

public class RoutingControl {
  private int routingType;
  private RoutingMother routingMother;

  public RoutingControl(Vector<Node> nodeList, Vector<Link> linkList,
      Vector<Pair> pairList, int routingType) {
    this.routingType = routingType;

    switch (routingType) {
      /**
       * Roteamento Fixo,Uma Rota.
       */
      case 0: {
        routingMother = new FixOneRoute(nodeList, linkList, pairList);
        break;
      }
      /**
       * Roteamento Adaptativo, Um Rota.
       */
      case 1: {
        routingMother = new LLROneRoute(nodeList, linkList);
        break;
      }
      /**
       * Roteamento Fixo, Duas Rotas.
       */
      case 2: {
        routingMother = new FixDoubleRoute(nodeList, linkList, pairList);
        break;
      }
      /**
       * Roteamento Fixo, Duas Rotas com backtracking.
       */
      case 3: {
        routingMother = new FixDoubleRouteBckt(nodeList, linkList, pairList);
        break;
      }
      /**
       * Roteamento Adaptativo, Duas Rotas.
       */
      case 4: {
        routingMother = new LLRDoubleRoute(nodeList, linkList);
        break;
      }
      /**
       * Roteamento Adaptativo, Duas Rotas com backtracking.
       */
      case 5: {
        routingMother = new LLRDoubleRoute(nodeList, linkList);
        break;
      }
      /**
       * Roteamento Active Restoration.
       */
      case 6: {
        routingMother = new ActiveRestoration(nodeList, linkList, pairList);
        break;
      }
      /**
       * Roteamento Fixo ou Adaptativo, em função da utilização da rede.
       * Uma rota
       */
      case 7: {
        routingMother = new FixLLROneRoute(nodeList, linkList,pairList);
        break;
      }
      /**
       * Roteamento Fixo ou Adaptativo, em função da utilização da rede.
       * Duas rota
       */
      case 8: {
        routingMother = new FixLLROneRoute(nodeList, linkList,pairList);
        break;
      }
      /**
       * Roteamento Fixo Alternativo!!!.
       */
      case 9: {
        System.out.println("Falta implementar!!!");
        break;
      }
    }
  }

  //----------------------------------------------------------------------------
  public int getRoutingType() {
    return this.routingType;
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna o objeto Roteamento.
   * @return RoutingMother
   */
  public RoutingMother getRoutingObject() {
    return this.routingMother;
  }

  //----------------------------------------------------------------------------
  /**
   * Caso implemente sobrevivência retorna duas rotas disjuntas.
   * Senão retorna uma rota.
   * @param p Pair
   * @return Route[]
   */
  public Vector<Route> getRoutes(Pair p) {
    return this.routingMother.getRoutes(p);
  }

}
