package simulator;

import java.util.*;
import network.*;
import request.*;

public class EventMachine {

  private Vector<Event> eventList;
  private double countEvent = 0;

  public EventMachine() {
    this.eventList = new Vector<Event> ();
  }

//------------------------------------------------------------------------------
  /**
   *Insere um evento na m�quina de eventos.
   * @param e Event
   */
  public void insert(Event e) {
    e.setId(this.countEvent);
    this.countEvent++;
    int i = 0;
    while (i < eventList.size() &&
           (eventList.elementAt(i)).getTime() < e.getTime()) {
      i++;
    }
    eventList.insertElementAt(e, i);
  }

//------------------------------------------------------------------------------
  /**
   *Inicia a execu��o da m�quina de eventos. A m�quina
   *de eventos executa at� n�o existir mais eventos
   *no eventList.
   */
  public void executeEvents() {
    while (eventList.size() > 0) {
      Event e = eventList.firstElement();
      eventList.removeElementAt(0);
      e.listener().execute(e);
    }
  }

  /**
   *Retorna o n�mero de eventos agendados (existente no eventList).
   * @return int
   */
  public int size() {
    return this.eventList.size();
  }

  /**
   *Finaliza a m�quina de eventos (limpa eventList).
   */
  public void stopMachine() {
    this.eventList.removeAllElements();
  }

  /**
   * remove o evento que contem a requisi��o
   * @param request RequestMother
   */
  public void remove(RequestMother request) {
    for (int i = 0; i < this.eventList.size(); i++) {
      Event event = this.eventList.get(i);
      if (event.getObject() instanceof RequestMother)
        if (event.getObject()==request ) {
          this.eventList.remove(event);
          break;
        }
    }
  }
}

