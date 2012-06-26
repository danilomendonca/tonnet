package gui;

import java.util.*;

import java.awt.*;

public class NoGrf extends network.Node{

private Point ponto;

  /**
   * Constroi um objeto NodGrf   
   */
  public NoGrf(Point p, String name) {
    super(name);
    ponto=p;
  }
  //---------------------------------------------------
  public int getX() {
    return ponto.x;
  }

//---------------------------------------------------
  public int getY() {
    return ponto.y;
  }

//---------------------------------------------------
  public void setX(int x) {
    ponto.x = x;
  }

//---------------------------------------------------
  public void setY(int y) {
    ponto.y = y;
  }
}
