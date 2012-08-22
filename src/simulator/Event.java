package simulator;

import network.*;

public class Event {

  private Object object;
  private EventListener eventListener;
  private double time;
  private double id;
  private boolean generateNext = true;

  /**
   *Constroi um evento.
   * @param r Object
   * @param eListener EventListener
   * @param time double
   */
  public Event(Object r, EventListener eListener, double time) {
    this.object = r;
    this.eventListener = eListener;
    this.time = time;
  }

  public void setId(double x){
    id=x;
  }

  public double getId() {
    return id;
  }
//------------------------------------------------------------------------------
  /**
   *Retorna qual � o escutador do evento.
   * @return EventListener
   */
  public EventListener listener() {
    return this.eventListener;
  }

//------------------------------------------------------------------------------
  /**
   *Retorna o tempo que o evento ser� disparado.
   * @return double
   */
  public double getTime() {
    return this.time;
  }

//------------------------------------------------------------------------------
  /**
   *Retorna o Objeto associado ao evento.
   * @return Object
   */
  public Object getObject() {
    return this.object;
  }

  /**
   * setObject
   * @param x Object
   */
  public void setObject(Object x) {
    this.object=x;
  }


  public boolean isGenerateNext(){
      return generateNext;
  }

  public void setGenerateNext(boolean generateNext){
      this.generateNext = generateNext;
  }

}

