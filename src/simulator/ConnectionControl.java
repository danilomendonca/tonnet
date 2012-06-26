package simulator;

import java.util.*;
import request.*;
import measurement.*;
import network.*;

public class ConnectionControl {
  private Measurements measurements;
  private Vector<RequestMother> connectionList;
  private double numberAffectedConnection=0;
  private double numberRepairedConnection=0;

  public ConnectionControl(Measurements m) {
    this.measurements=m;
    connectionList = new Vector<RequestMother> ();
  }

  /**
   *Executa a estratégia de sobrevivência para todas as conexões afetadas
   * pela falha do link detectada. O Vector contem o link de ida e o de volta.
   * @param listLink Vector
   * @param eMachine EventMachine
   */
  public void failureDetect(Vector<Link> listLink,EventMachine eMachine) {
    this.numberAffectedConnection=0;
    this.numberRepairedConnection=0;

    for (int i = 0; i < listLink.size(); i++) {
      connectionAffected(listLink.get(i),eMachine);
    }

    if (numberAffectedConnection>0){
      this.measurements.incNumGeneratedFailureEffective();
      this.measurements.sumRestorability(numberRepairedConnection /
                                         numberAffectedConnection);
    }
  }

  /**
   * retorna true se a falha do link afetou algum das requisições em atual
   * atendimento. Se alguma requisição foi afetada é iniciada a estratégia de
   * sobrevivência. Contabiliza o número de requisições que sobreviveram a
   * falha do link e o número de requisições que foram afetadas com a falha do link
   * @param link Link
   * @param eMachine EventMachine
   */
  private void connectionAffected(Link link, EventMachine eMachine) {
    Vector<RequestMother> listRequestAux = new Vector<RequestMother>();

    for (int i = 0; i < this.connectionList.size(); i++) {
      if (this.connectionList.get(i).requestAffected(link)){
          this.numberAffectedConnection++;
          if(this.surviveConnection(this.connectionList.get(i), link,eMachine)){
            this.numberRepairedConnection++;
          }
          else{
            /**
             * Armazena as requisições que não foram recuperadas. As rotas
             * dessas requisição são definidas com null no método survive de
             * toda classe request ou filha.
             */
            if (this.connectionList.get(i).getRoute()==null)
              listRequestAux.add(this.connectionList.get(i));
          }
      }
    }

    // remove da lista de conexões em atendimento as requisições que não fora
    // recuperadas.
    if (listRequestAux.size() > 0)
      this.connectionList.removeAll(listRequestAux);

  }

  /**
   * retorna true se a conexão sobreviveu a falha. caso contrário retorna false.
   * @param request RequestMother
   * @param link Link
   * @param eMachine EventMachine
   * @return boolean
   */
  private boolean surviveConnection(RequestMother request, Link link, EventMachine eMachine ) {
    return request.survive(link,eMachine);
  }

  /**
   * adiciona request a lista de requisições em atual atendimento
   * @param request Request
   */
  public void addRequest(RequestMother request) {
    connectionList.add(request);
  }
  /**
   * remove request da lista de requisições em atual atendimento
   * @param request Request
   */
  public void removeRequest(RequestMother request) {
    connectionList.remove(request);
  }

}

