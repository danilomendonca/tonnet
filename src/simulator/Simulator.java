package simulator;

import network.*;
import request.*;
import measurement.*;

public class Simulator {

  private EventMachine eMachine;
  private ArriveRequest arriveRequest;
  private OccurFailure occurFailure;
  private Simulation simulation;

  /**
   *Constroi um obj Simulator.
   * @param simulation Simulation
   */
  public Simulator(Simulation simulation) {
    this.simulation = simulation;
  }

//------------------------------------------------------------------------------
  /**
   * Inicia a Simula��o. Para isso � necess�rio: (1) carregar a malha; (2)criar
   * as inst�ncias de arriveRequest e FinalizeRequest; (3) agendar os primeiro
   * eventos de chegada de requisi��o.
   * @return Measurements
   * @param failure boolean
   * @param fixLinkRate double
   * @param occurRate double
   */
  public Measurements start(boolean failure, double fixLinkRate,double occurRate) {
    eMachine = new EventMachine();
    // criando o escutador de eventos arriveRequest
    arriveRequest = new ArriveRequest(this.simulation.getMesh(),
                                      this.getEventMachine(),
                                      this.simulation.getTotalNumberOfRequest(),
                                      this.simulation.getHoldRate(),
                                      this.simulation.getArrivedRate(),
                                      this.simulation.getWAAlgorithm());


    int numMaxFailure = 20000000;

    // criando o escutador de eventos occurFailure
    occurFailure = new OccurFailure(this.simulation.getMesh(),
                                    this.simulation.getTotalNumberOfRequest(),
                                    this.getEventMachine(),
                                    numMaxFailure,
                                    fixLinkRate,
                                    occurRate);

    this.scheduleFirstEvents(failure);
    this.eMachine.executeEvents();
    this.reports();
    return this.simulation.getMesh().getMeasurements();

  }

  /**
   * reports
   */
  private void reports() {
    //calcula as medidas dos links (num falhas, utilizacao por link)
    this.getSimulation().getMesh().calculateMeasureLink();
    //calcula as medidas dos pares (bloq por par e n� de vezes q o par foi gerado)
    this.getSimulation().getMesh().calculateMeasurePair();
    //calcula as medidas dos n�s
    this.getSimulation().getMesh().calculateMeasureNode();

    //---------------------------------------
    /*
         System.out.println("num. de ocorrecias de + de 1 falha: "+simulation.getMesh().countMoreOneFailure);
         System.out.println("num. de falhas: "+simulation.getMesh().getMeasurements().getNumGeneratedFailure());
     */
  }

  //------------------------------------------------------------------------------
  /**
   * Agenda o primeiro eventos de chegada de requisi��o. Defini��o do tipo de
   * simula��o.
   * @param failure boolean
   */
  private void scheduleFirstEvents(boolean failure) {
    double tempo = this.simulation.getMesh().getRandomVar().negexp(simulation.
        getArrivedRate());
    RequestMother r = null;

    /*
         0. SS  - Fix
         1. SS  - Fix Alternativo
         2. SS  - LLR
         3. SS  - Fixo-ADP

         10.TS  - Fix
         11.TS  - LLR
         12.TSB - Fix
         13.TSB - LLR

         20.RS  - Rota primaria Fixa e Rota Secundaria LLR.(S/ implementa��o!!)
         21.RS  - Fixo
         22.RS  - LLR
         23.RS  - Fixo-ADP
         24.AR  - Fix
         25.AR  - LLR  (Sem implementa��o!!)

         30.Adaptive (RS + TSB) - Fix
         31.Adaptive (RS + TSB) - LLR
     */
    switch (this.simulation.getSimulationType()) {

      /**-----------------------------------------------------------------------
       * N�O IMPLEMENTA T�CNICA DE SOBREVIV�NCIA
       * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
       */
      case 0:
        this.simulation.getMesh().startRouteControl(0);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());
        break;
        /**-----------------------------------------------------------------------
         * N�O IMPLEMENTA T�CNICA DE SOBREVIV�NCIA
         * ROTEAMENTO FIXO ALTERNATIVO K=3 ROTAS DISTUNTAS (AINDA N�O FOI TESTADO !!!!!!!!!)
         */
      case 1:
        this.simulation.getMesh().startRouteControl(9);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());

        break;
        /**-----------------------------------------------------------------------
         * N�O IMPLEMENTA T�CNICA DE SOBREVIV�NCIA
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 2:
        this.simulation.getMesh().startRouteControl(1);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());
        break;
        /**-----------------------------------------------------------------------
         * N�O IMPLEMENTA T�CNICA DE SOBREVIV�NCIA
         * ROTEAMENTO FIXO - ADAPTATIVO(LLR)
         */
      case 3:
        this.simulation.getMesh().startRouteControl(7);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         *TWO STEP
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
         */
      case 10:
        this.simulation.getMesh().startRouteControl(2);
        r = new ReqDPPTwoStep(this.simulation.getMesh().pairGenerator(),
                              this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         * TWO STEP
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 11:
        this.simulation.getMesh().startRouteControl(4);
        r = new ReqDPPTwoStep(this.simulation.getMesh().pairGenerator(),
                              this.simulation.getMesh());
        break;

        /**-----------------------------------------------------------------------
         * TWO STEP COM BACKTRACKING
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
         */
      case 12:
        this.simulation.getMesh().startRouteControl(3);
        r = new ReqDPPTwoStep(this.simulation.getMesh().pairGenerator(),
                              this.simulation.getMesh());
        break;

        /**-----------------------------------------------------------------------
         * TWO STEP COM BACKTRACKING
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 13:
        this.simulation.getMesh().startRouteControl(5);
        r = new ReqDPPTwoStep(this.simulation.getMesh().pairGenerator(),
                              this.simulation.getMesh());
        break;

        /**---------------------------------------------------------------------
         * RESTAURA��O COM BACKTRACKING (ROTA PRIM�RIA = FIXA;
         * ROTA DE RESTAURA��O � CALCULADA COM LLR) (FALTA IMPLEMENTAR !!!!!!!!)
         */
      case 20:
        System.out.println("FALTA IMPLEMENTAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        break;
        /**---------------------------------------------------------------------
         * RESTAURA��O SIMPLES COM BACKTRACKIN.
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
         * A ROTA DE RESTAURA��O � A MESMA ROTA DE BACKUP DO TWO STEP COM BACKTRACKING.
         */
      case 21:
        this.simulation.getMesh().startRouteControl(3);
        r = new ReqSimpleRestoration(this.simulation.getMesh().pairGenerator(),
                                     this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         * RESTAURA��O SIMPLES COM BACKTACKING.
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 22:
        this.simulation.getMesh().startRouteControl(5);
        r = new ReqSimpleRestoration(this.simulation.getMesh().pairGenerator(),
                                     this.simulation.getMesh());
        break;

        /**---------------------------------------------------------------------
         * RESTAURA��O SIMPLES COM BACKTRACKIN.
         * Roteamento Fixo-ADP:
         * SE UTILIZA��O DA REDE FOR MENOR QUE FATOR U
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
         * SENAO ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 23:
        this.simulation.getMesh().startRouteControl(8);
        r = new ReqSimpleRestoration(this.simulation.getMesh().pairGenerator(),
                                     this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         * ACTIVE RESTORATION COM BACKTRACKING.
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
         */
      case 24:
        this.simulation.getMesh().startRouteControl(6);
        r = new ReqActRestoration(this.simulation.getMesh().pairGenerator(),
                                  this.simulation.getMesh());
        break;

        /**---------------------------------------------------------------------
         * ACTIVE RESTORATION.
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 25:
        System.out.println("FALTA IMPLEMENTAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        break;
        /**---------------------------------------------------------------------
         * ADAPTIVE (SR + TSB)
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = N� DE SALTOS)
         */
      case 30:
        this.simulation.getMesh().startRouteControl(3);
        r = new AdaptiveSRTSB(this.simulation.getMesh().pairGenerator(),
                              this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         * ADAPTIVE (SR + TSB)
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 31:
        this.simulation.getMesh().startRouteControl(3);
        r = new AdaptiveSRTSB(this.simulation.getMesh().pairGenerator(),
                              this.simulation.getMesh());
        break;

    }

    //Verifica se a simula��o vai gerar falhas
    if (failure) {
      this.occurFailure.scheduleFailureEvent(0);
    }

    Event e = new Event(r, this.arriveRequest, tempo);
    this.eMachine.insert(e);

  }

//------------------------------------------------------------------------------
  public EventMachine getEventMachine() {
    return this.eMachine;
  }

//------------------------------------------------------------------------------
  public Simulation getSimulation() {
    return this.simulation;
  }
}
