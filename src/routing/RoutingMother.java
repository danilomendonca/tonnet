package routing;

import java.util.*;
import network.*;

public abstract class RoutingMother {
  /**
   * Lista de nos da rede.
   */
  protected Vector<Node> nodeList;
  /**
   * Lista de links da rede.
   */
  protected Vector<Link> linkList;
  /**
   * Representa um custo infinito para um nó.
   */
  private final int infinite = Integer.MAX_VALUE;

  public RoutingMother(Vector<Node> nodeList, Vector<Link> linkList) {
    this.nodeList = nodeList;
    this.linkList = linkList;
  }

  //------------------------------------------------------------------------------
  /**
   * A partir de uma lista de nós, retorna o nó que tem o menor length
   * Apenas o shortestePath() utiliza este metodo.
   * @param nodes Vector.
   * @return Node.less-length Node of nodes.
   */
  private Node lessLengthNode(Vector<Node> nodes) {
    Node n = (Node) nodes.firstElement();

    for (int i = 1; i < nodes.size(); i++) {
      if ( (nodes.get(i).getMenRouting()).getLength() <
          n.getMenRouting().getLength()) {
        n = nodes.get(i);
      }
    }
    nodes.remove(n);
    return n;
  }

  //----------------------------------------------------------------------------
  /**
   * Encontra o menor caminho entre os nós s e d.
   * Ref. Book: Estrutura de Dados e Algoritmos. Bruno R. Praiss. Ed Campus. pag. 512.
   * @param s Node
   * @param t Node
   * @return Route with shortest Path.
   */
  protected Route shortestPath(Node s, Node t) {

    for (int i = 0; i < nodeList.size(); i++) {
      Node auxNode;
      auxNode = nodeList.get(i);
      auxNode.getMenRouting().setPredecessor(null);
      auxNode.getMenRouting().setLength(infinite);
      auxNode.getMenRouting().setKnown(false);
    }

    s.getMenRouting().setLength(0);
    Vector priorityList = new Vector(); //substituir Vector por PriorityQueue
    priorityList.add(s);

    boolean find = false;
    while ( (!priorityList.isEmpty()) && (!find)) {
      Node nodeA = lessLengthNode(priorityList);
      if (nodeA == t) {
        find = true;
      }
      if (!nodeA.getMenRouting().isKnown()) {
        nodeA.getMenRouting().setKnown(true);
        Enumeration e = nodeA.getOxc().getLinksList().elements();

        while ( (e.hasMoreElements()) && (!find)) {
          Link l = (Link) e.nextElement();
          Node nodeB = this.getNode(l.getDestination().getName());
          double costL = l.getCost();

          if (costL != -1) {
            double d = nodeA.getMenRouting().getLength() + costL;
            if (nodeB.getMenRouting().getLength() > d) {
              nodeB.getMenRouting().setLength(d);
              nodeB.getMenRouting().setPredecessor(nodeA.getMenRouting());
              priorityList.add(nodeB);
            }
          }
        }
      }
    }

    Vector path = new Vector();
    if (t.getMenRouting().getPredecessor() != null) {
      do {
        path.insertElementAt(t, 0);
        MenRouting mR = t.getMenRouting().getPredecessor();
        if (mR != null) {
          t = this.getNode(mR.getName());
        }
        else {
          t = null;
        }
      }
      while (t != null);
    }

    if (path != null) {
      if (path.size() > 1) {
        return (new Route(path));
      }
    }
    return null;
  }

  //------------------------------------------------------------------------------
  /**
   * Retorna um nó a partir do nome
   * @param name String
   * @return Node
   */
  protected Node getNode(String name) {
    for (int i = 0; i < this.nodeList.size(); i++) {
      Node tmp = nodeList.get(i);
      if (tmp.getName().equals(name)) {
        return tmp;
      }
    }
    return null;
  }

  //...................................................................
  public abstract Vector<Route> getRoutes(Pair p);
}
