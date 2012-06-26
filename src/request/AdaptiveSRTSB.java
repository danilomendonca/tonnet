package request;

import network.*;
import routing.*;
import simulator.*;

public class AdaptiveSRTSB
        extends RequestMother {
    protected Route routeBackup;
    protected int[] waveListRBackup;
    boolean protection;
    
    public AdaptiveSRTSB(Pair p, Mesh mesh) {
        super(p, mesh);
        this.protection = true;
    }
    
    //------------------------------------------------------------------------------
    public boolean isProtection() {
        return protection;
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
    public boolean RWA() {
        boolean establish;
        if (this.mesh.calculateUtilization() > 0.45) {
            protection = true;
            establish = fixedDedicatedBackupWithFailure();
            if (establish) {
                establishConnection();
            }
            
        } else {
            protection = false;
            establish = routing();
            if (establish) {
                establishConnection();
            }
            
        }
        
        return establish;
    }
    
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
        
        if (this.protection) {
            //rota secundaria
            for (int i = 0; i < this.waveListRBackup.length - 1; i++) {
                if (this.waveListRBackup[i] != this.waveListRBackup[i + 1]) {
                    if (!this.routeBackup.getNode(i + 1).useWc()) {
                        System.err.println("erro ao usar conversor rB");
                    }
                }
            }
            
            this.routeBackup.useWavelength(this.waveListRBackup);
        }
    }
    
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
        if (this.protection) {
            //rota secundaria
            for (int i = 0; i < this.waveListRBackup.length - 1; i++) {
                if (this.waveListRBackup[i] != this.waveListRBackup[i + 1]) {
                    if (!this.routeBackup.getNode(i + 1).liberateWC()) {
                        System.err.println("erro ao usar conversor");
                    }
                }
            }
            
            this.routeBackup.liberateWavelength(this.waveListRBackup);
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * backup dedicado fixo com geração de falha
     * @return boolean
     */
    protected boolean fixedDedicatedBackupWithFailure() {
        Route routeAux, routeBackupAux;
        routeAux = this.mesh.getRoutingControl().getRoutes(this.pair).get(0);
        routeBackupAux = this.mesh.getRoutingControl().getRoutes(this.pair).get(1);
        /**
         * É necessário testar se routeAux e RouteBackupAux são null pois o
         * roteamento pode não encontrar uma rota para o par de nós em questão
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
    
    protected boolean routing() {
        Route routeAux;
        routeAux = this.mesh.getRoutingControl().getRoutes(this.pair).get(0);
        if (routeAux != null) {
            this.setRoute(routeAux);
            return this.route.tryEstablish(this.waveList, this.mesh,
                    this.pair.getCategory(), true);
        }
        return false;
    }
    
//------------------------------------------------------------------------------
    /**
     * retorna uma nova requisição do mesmo tipo
     * @param p Pair
     * @param m Mesh
     * @return RequestMother
     */
    public RequestMother getNewRequest(Pair p, Mesh m) {
        return new AdaptiveSRTSB(p, m);
        
    }
    
    //------------------------------------------------------------------------------
    public boolean survive(Link link, EventMachine eMachine) {
        if (protection == true) {
            if ( (this.route.anyLinkFaild() == true) &&
                    (this.routeBackup.anyLinkFaild() == true)) {
                return false;
            } else {
                return true;
            }
        } else {
            this.tearDownConnection();
            Route restoration = this.mesh.getRoutingControl().getRoutes(this.pair).get(
                    1);
            boolean flag;
            if (restoration != null) {
                this.setRoute(restoration);
                flag = this.route.tryEstablish(this.waveList, this.mesh,
                        this.pair.getCategory(), true);
            } else {
                flag = false;
            }
            
            if (flag == true) {
                //estabelecer a rota de restauração
                this.establishConnection();
            } else {
                this.setRoute(null);
                eMachine.remove(this);
            }
            return flag;
            
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * sempre retornara falso pois a rota de backup é disjunta
     * @param link Link
     * @return boolean
     */
    public boolean requestAffected(Link link) {
        if (route!=null)
            if (this.route.anyLinkFaild() == true)
                return true;
        if (this.protection)
            if (routeBackup!=null)
                if (this.routeBackup.anyLinkFaild() == true)
                    return true;
        return false;
    }
}
