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
   * Inicia a Simulação. Para isso é necessário: (1) carregar a malha; (2)criar
   * as instâncias de arriveRequest e FinalizeRequest; (3) agendar os primeiro
   * eventos de chegada de requisição.
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
    //calcula as medidas dos pares (bloq por par e nº de vezes q o par foi gerado)
    this.getSimulation().getMesh().calculateMeasurePair();
    //calcula as medidas dos nós
    this.getSimulation().getMesh().calculateMeasureNode();

    //---------------------------------------
    /*
         System.out.println("num. de ocorrecias de + de 1 falha: "+simulation.getMesh().countMoreOneFailure);
         System.out.println("num. de falhas: "+simulation.getMesh().getMeasurements().getNumGeneratedFailure());
     */
  }

  //------------------------------------------------------------------------------
  /**
   * Agenda o primeiro eventos de chegada de requisição. Definição do tipo de
   * simulação.
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

         20.RS  - Rota primaria Fixa e Rota Secundaria LLR.(S/ implementação!!)
         21.RS  - Fixo
         22.RS  - LLR
         23.RS  - Fixo-ADP
         24.AR  - Fix
         25.AR  - LLR  (Sem implementação!!)

         30.Adaptive (RS + TSB) - Fix
         31.Adaptive (RS + TSB) - LLR
     */
    switch (this.simulation.getSimulationType()) {

      /**-----------------------------------------------------------------------
       * NÃO IMPLEMENTA TÉCNICA DE SOBREVIVÊNCIA
       * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
       */
      case 0:
        this.simulation.getMesh().startRouteControl(0);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());
        break;
        /**-----------------------------------------------------------------------
         * NÃO IMPLEMENTA TÉCNICA DE SOBREVIVÊNCIA
         * ROTEAMENTO FIXO ALTERNATIVO K=3 ROTAS DISTUNTAS (AINDA NÃO FOI TESTADO !!!!!!!!!)
         */
      case 1:
        this.simulation.getMesh().startRouteControl(9);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());

        break;
        /**-----------------------------------------------------------------------
         * NÃO IMPLEMENTA TÉCNICA DE SOBREVIVÊNCIA
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 2:
        this.simulation.getMesh().startRouteControl(1);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());
        break;
        /**-----------------------------------------------------------------------
         * NÃO IMPLEMENTA TÉCNICA DE SOBREVIVÊNCIA
         * ROTEAMENTO FIXO - ADAPTATIVO(LLR)
         */
      case 3:
        this.simulation.getMesh().startRouteControl(7);
        r = new Request(this.simulation.getMesh().pairGenerator(),
                        this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         *TWO STEP
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
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
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
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
         * RESTAURAÇÃO COM BACKTRACKING (ROTA PRIMÁRIA = FIXA;
         * ROTA DE RESTAURAÇÃO É CALCULADA COM LLR) (FALTA IMPLEMENTAR !!!!!!!!)
         */
      case 20:
        System.out.println("FALTA IMPLEMENTAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        break;
        /**---------------------------------------------------------------------
         * RESTAURAÇÃO SIMPLES COM BACKTRACKIN.
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
         * A ROTA DE RESTAURAÇÃO É A MESMA ROTA DE BACKUP DO TWO STEP COM BACKTRACKING.
         */
      case 21:
        this.simulation.getMesh().startRouteControl(3);
        r = new ReqSimpleRestoration(this.simulation.getMesh().pairGenerator(),
                                     this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         * RESTAURAÇÃO SIMPLES COM BACKTACKING.
         * ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 22:
        this.simulation.getMesh().startRouteControl(5);
        r = new ReqSimpleRestoration(this.simulation.getMesh().pairGenerator(),
                                     this.simulation.getMesh());
        break;

        /**---------------------------------------------------------------------
         * RESTAURAÇÃO SIMPLES COM BACKTRACKIN.
         * Roteamento Fixo-ADP:
         * SE UTILIZAÇÃO DA REDE FOR MENOR QUE FATOR U
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
         * SENAO ROTEAMENTO ADAPTATIVO (LLR)
         */
      case 23:
        this.simulation.getMesh().startRouteControl(8);
        r = new ReqSimpleRestoration(this.simulation.getMesh().pairGenerator(),
                                     this.simulation.getMesh());
        break;
        /**---------------------------------------------------------------------
         * ACTIVE RESTORATION COM BACKTRACKING.
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
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
         * ROTEAMENTO FIXO DE MENOR CAMINHO (CUSTO = Nº DE SALTOS)
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

    //Verifica se a simulação vai gerar falhas
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
