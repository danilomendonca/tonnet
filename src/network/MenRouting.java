package network;

public class MenRouting {
  private String name;
  private boolean known;
  private double length;
  private MenRouting predecessor;

  /**
   * Creates a new instance of MenRouting.
   * @param name String
   */
  public MenRouting(String name) {
    this.name = name = name;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property known.
   * @return true if is known; false otherwise.
   */
  public boolean isKnown() {
    return known;
  }

//------------------------------------------------------------------------------
  /**
   * Setter for property known.
   * @param known boolean New value of property known.
   */
  public void setKnown(boolean known) {
    this.known = known;
  }

//------------------------------------------------------------------------------
  /**
   * Setter for property predecessor.
   * @param x MenRouting New value of property predecessor.
   */
  public void setPredecessor(MenRouting x) {
    predecessor = x;
  }

//------------------------------------------------------------------------------
  /**
   * Setter for property codigo.
   * @param l double New value of property .
   */
  public void setLength(double l) {
    length = l;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property name
   * @return String name name this Node.
   */
  public String getName() {
    return name;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property length.
   * @return double length
   */
  public double getLength() {
    return length;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property predecessor.
   * @return MenRouting predecessor.
   */
  public MenRouting getPredecessor() {
    return predecessor;
  }

}
