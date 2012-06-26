package network;

import java.util.Vector;

public class Oxc {

  private String name;
  private Vector<Link> linksList;

//------------------------------------------------------------------------------
  /**
   * Creates a new instance of Oxc.
   * @param name String name Oxc
   */
  public Oxc(String name) {
    this.linksList = new Vector<Link>();
    this.name=name;
  }
//------------------------------------------------------------------------------
  /**
   * Getter for property name
   * @return String name this Oxc.
   */
  public String getName() {
    return name;
  }
//------------------------------------------------------------------------------
  
 /**
  * Setter for property name.
  * @param name nome
  */
  public void setName(String name) {
    this.name=name;
  }
//------------------------------------------------------------------------------
  /**
   * Add link for Oxc next.
   * @param next Oxc.
   * @param c double Oxc value of property cost at Oxc next.
   * @param numberWavelength int Oxc value of property numWave at new link.
   * @return true case successufully added Link for Oxc next; false otherwise.
   */
  public boolean addLink(Oxc next, double c, int numberWavelength) {
    return linksList.add(new Link(this, next, c, numberWavelength));
  }

//------------------------------------------------------------------------------
  /**
   * Add Link l
   * @param link Link
   * @return true case successufully added Link l; false otherwise.

   */
  public boolean addLink(Link link) {
    if (link.getSource().equals(this)) {
      linksList.add(link);
      return true;
    }
    return false;
  }

//------------------------------------------------------------------------------
  /**
   * Return Link to Oxc n.
   * @param n Oxc.
   * @return Link link to Oxc n.
   */
  public Link linkTo(Oxc n) {
    Link auxLink;
    for (int i = 0; i < linksList.size(); i++) {
      auxLink = linksList.get(i);
      if (auxLink.adjacent(n))
        return auxLink;
    }
    return null;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property linksList
   * @return Vector with links
   */
  public Vector<Link> getLinksList() {
    return linksList;
  }

//------------------------------------------------------------------------------
  /**
   * Remove Link to Oxc n
   * @param n Oxc
   * @return true case successufully removed Link to Oxc n; false otherwise.
   */
  public boolean removeLink(Oxc n) {
    Link auxLink;
    for (int i = 0; i < linksList.size(); i++) {
      auxLink = linksList.get(i);
      if (auxLink.adjacent(n)) {
        return linksList.remove(auxLink);
      }
    }
    return false;
  }

//------------------------------------------------------------------------------
  /**
   * Is Oxc x adjacent of this Oxc.
   * @param x Oxc
   * @return true if Oxc x is adjacent of this Oxc; false otherwise.
   */
  public boolean isAdjacent(Oxc x) {
    Link auxLink;

    for (int i = 0; i < linksList.size(); i++) {
      auxLink = linksList.get(i);
      if (auxLink.adjacent(x))
        return true;
    }
    return false;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property cost for Oxc x.
   * @param x Oxc.
   * @return double cost.
   */
  public double getCost(Oxc x) {
    Link auxLink;
    for (int i = 0; i < linksList.size(); i++) {
      auxLink = linksList.get(i);
      if (auxLink.adjacent(x))
        return auxLink.getCost();
    }
    //System.out.println("erro: O nó "+ x.getName()+" nao é adjacente ao "+getName());
    return -1;
  }

//------------------------------------------------------------------------------
  /**
   * Return all adjacents Oxcs of this node.
   * @return Vector with all adjacentes
   */
  public Vector<Node> getAllAdjacents() {
    Vector list = new Vector();
    Link auxLink;
    for (int i = 0; i < linksList.size(); i++) {
      auxLink = linksList.get(i);
        list.add(auxLink.getDestination());
    }
    return list;
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna o numero de interfaces do nó
   * @return int
   */
  public int getNumInterfaces(){
    return this.linksList.size();
  }
}
