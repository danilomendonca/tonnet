package request;

import java.util.Vector;
import network.*;
import routing.Route;
import simulator.EventMachine;
import measurement.*;

public class ReqDPPTwoStep
        extends RequestMother {
    protected Route routeBackup;
    protected int[] waveListRBackup;
    
    public ReqDPPTwoStep(Pair p, Mesh mesh) {
        super(p, mesh);
        this.protection = true;
    }
    
    //------------------------------------------------------------------------------
    public void setRouteBackup(Route r) {
        this.routeBackup = r;
        this.waveListRBackup = new int[this.routeBackup.getHops()];
    }
    
//------------------------------------------------------------------------------
    public void setWaveListRBackup(int index, int wave) {
        this.waveListRBackup[index] = wave;
    }
    
//------------------------------------------------------------------------------
    public Route getRouteBackup() {
        return this.routeBackup;
    }
    
    //------------------------------------------------------------------------------
    /**
     * backup dedicado fixo com gera��o de falha
     * @return boolean
     */
    protected boolean routing() {
        Route routeAux, routeBackupAux;
        Vector<Route>
                routes = this.mesh.getRoutingControl().getRoutes(this.pair);
        routeAux = routes.get(0);
        routeBackupAux = routes.get(1);
        /**
         * � necess�rio testar se routeAux e RouteBackupAux s�o null pois o
         * roteamento pode n�o encontrar uma rota para o par de n�s em quest�o
         */
        if (routeAux != null) {
            this.setRoute(routeAux);
        }
        if (routeBackupAux != null) {
            this.setRouteBackup(routeBackupAux);
        }
        
        if (this.route != null) { //existe rota primaria
            boolean flagPrimary = this.route.tryEstablish(this.waveList, this.mesh,
                    this.pair.getCategory(), true);
            if (this.routeBackup != null) { //existe rota secundaria
                boolean flagBackup = this.routeBackup.tryEstablish(this.waveListRBackup,
                        this.mesh, this.pair.getCategory(), true);
                if (flagBackup == false) { //incrementa bloqueio por ausencia de backup (falta recursos na rota backup)
                    this.mesh.getMeasurements().incBlockingAbsenceBackupProtection();
                }
                return (flagPrimary && flagBackup);
            } else { //incrementa bloqueio por ausencia de backup (nao existe rota de backup)
                this.mesh.getMeasurements().incBlockingAbsenceBackupProtection();
            }
        }
        return false;
    }
    
    //------------------------------------------------------------------------------
    /**
     *Define que os comprimentos de onda alocados ficam ocupados nos
     *enlaces das rotas prim�ria e segund�ria utilizadas pela requisi��o.
     */
    protected void establishConnection() {
        //rota primaria
        for (int i = 0; i < this.waveList.length - 1; i++) {
            if (this.waveList[i] != this.waveList[i + 1]) {
                if (!this.route.getNode(i + 1).useWc()) {
                    System.err.println("erro ao usar conversor");
                }
            }
        }
        
        this.route.useWavelength(this.waveList);
        
        //rota secundaria
        for (int i = 0; i < this.waveListRBackup.length - 1; i++) {
            if (this.waveListRBackup[i] != this.waveListRBackup[i + 1]) {
                if (!this.routeBackup.getNode(i + 1).useWc()) {
                    System.err.println("erro ao usar conversor");
                }
            }
        }
        
        this.routeBackup.useWavelength(this.waveListRBackup);
        
    }
    
//------------------------------------------------------------------------------
    /**
     *Define que os comprimentos de onda alocados ficam livres nos
     *enlaces da rota utilizada pela requisi��o.
     */
    public void tearDownConnection() {
        //rota primaria
        for (int i = 0; i < this.waveList.length - 1; i++) {
            if (this.waveList[i] != this.waveList[i + 1]) {
                if (!this.route.getNode(i + 1).liberateWC()) {
                    System.err.println("erro ao liberar conversor");
                }
            }
        }
        
        this.route.liberateWavelength(this.waveList);
        
        //rota secundaria
        for (int i = 0; i < this.waveListRBackup.length - 1; i++) {
            if (this.waveListRBackup[i] != this.waveListRBackup[i + 1]) {
                if (!this.routeBackup.getNode(i + 1).liberateWC()) {
                    System.err.println("erro ao liberar conversor");
                }
            }
        }
        
        this.routeBackup.liberateWavelength(this.waveListRBackup);
    }
    
    //------------------------------------------------------------------------------
    /**
     * retorna uma nova requisi��o do mesmo tipo
     * @param p Pair
     * @param m Mesh
     * @return RequestMother
     */
    public RequestMother getNewRequest(Pair p, Mesh m) {
        return new ReqDPPTwoStep(p, m);
        
    }
    
    //------------------------------------------------------------------------------
    /**
     * retorna true se pelo menos uma das duas rotas prim�ria e secund�ria for
     * afetada pela falha. Marca a rota afedada.
     * @param link Link
     * @return boolean
     */
  /*  public boolean requestAffected(Link link) {
      boolean flag = false;
      // verifica se a requisi��o
      if ( (route.anyLinkFaild()) && (routeBackup.anyLinkFaild())) {
        return false;
      }
      else {
        if (route.anyLinkFaild()) {
          flag = true;
        }
        if (routeBackup.anyLinkFaild()) {
          flag = true;
        }
      }
      return flag;
    } */
//------------------------------------------------------------------------------
    /**
     * Survive
     * @param link Link
     * @param eMachine EventMachine
     * @return boolean
     */
    public boolean survive(Link link, EventMachine eMachine) {
        if ( (route.anyLinkFaild() == true) && (routeBackup.anyLinkFaild() == true)) {
            return false;
        } else {
            return true;
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * sempre retornara falso pois a rota de backup � disjunta
     * @param link Link
     * @return boolean
     */
    public boolean requestAffected(Link link) {
        if (route!=null)
            if (this.route.anyLinkFaild() == true)
                return true;
        if (routeBackup!=null)
            if (this.routeBackup.anyLinkFaild() == true)
                return true;
        return false;
    }
}