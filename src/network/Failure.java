package network;

import java.util.*;

public class Failure {
  private Vector<Link> listLink;


  public Failure(Vector<Link> Links) {
    this.listLink=Links;
  }

  /**
   * Repara os enlaces associados a esta falha.
   *
   * @return boolean
   */
  public boolean fixFailure() {
    for (int i = 0; i < listLink.size(); i++) {
      listLink.get(i).fixLink();
    }
    return false;
  }
  /**
   * configura todos os link associados como links com falha.
   * @return boolean
   */
  public boolean fail() {
    for (int i = 0; i < listLink.size(); i++) {
      listLink.get(i).setFaild();
    }
    return false;
  }

  /**
   * printLinks
   */
  public void printLinks() {
    String nome;
    for (int i = 0; i < listLink.size(); i++) {
      nome = "<" + listLink.get(i).getSource().getName() + "," +
          listLink.get(i).getDestination().getName()+">";
  System.out.print("+"+ nome);
    }
    System.out.println("");
  }


  public Vector<Link> getListLink() {
    return this.listLink;
  }

}
