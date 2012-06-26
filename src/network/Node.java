package network;

import java.io.Serializable;
import java.util.Vector;

public class Node implements Serializable {
    
    private String name;
    private MenRouting menRouting;
    private Oxc oxc;
    private WcBank wcBank;
    private int privilege;
    private boolean wcrTotal;
    
    /**
     * Creates a new instance of Node.
     * @param name String
     */
    public Node(String name) {
        this.name = name;
        this.menRouting = new MenRouting(name);
        this.oxc = new Oxc(name);
        this.privilege = 1;
        this.wcrTotal = false;
    }
    
    /**
     * Creates a new instance of Node.
     * @param name String
     * @param privilege int
     */
    public Node(String name, int privilege) {
        this.name=name;
        this.setPrivilege(privilege);
        this.menRouting = new MenRouting(name);
        this.oxc = new Oxc(name);
    }
    
//------------------------------------------------------------------------------
    public void setName(String name){
        this.name = name;
        this.oxc.setName(name);
    }
//------------------------------------------------------------------------------
    /**
     * Getter for property menRouting
     * @return enRouting MenRouting this Node.
     */
    public MenRouting getMenRouting() {
        return this.menRouting;
    }
    
//------------------------------------------------------------------------------
    /**
     * Getter for property oxc
     * @return Oxc oxc this Node.
     */
    public Oxc getOxc() {
        return this.oxc;
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
     * Retorna o privilegio do nó
     * @return int
     */
    public int getPrivilege() {
        return this.privilege;
    }
    
    //------------------------------------------------------------------------------
    /**
     * Retorna banco de conversores
     * @return WcBank
     */
    public WcBank getWcBank() {
        return this.wcBank;
    }
    
    //------------------------------------------------------------------------------
    /**
     * configura o privilegio do nó
     * @param privilege int
     */
    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
    
    //------------------------------------------------------------------------------
    /**
     * Configura o nó para um WCR
     * @param numWcs int
     */
    public void setWcr(int numWcs) {
        if (numWcs>0){
            this.wcBank = new WcBank(numWcs);
        }else
        this.wcBank=null;
    }
    
    //------------------------------------------------------------------------------
    /**
     * retorna true se a conversão foi realizada com sucesso
     * @return boolean
     */
    public boolean useWc() {
        return this.getWcBank().useWC();
    }
    
    //------------------------------------------------------------------------------
    /**
     * retorna true se o WC foi liberado com sucesso
     * @return boolean
     */
    public boolean liberateWC() {
        return this.getWcBank().liberateWC();
    }
    
    //------------------------------------------------------------------------------
    /**
     * retorna true se o nó for WCR.
     * @return boolean
     */
    public boolean isWCR() {
        if (getWcBank() == null) {
            return false;
        } else {
            return true;
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * retorna true se o nó for um WCR e possuir conversores livres. Caso contrário
     * retorna false.
     * @return boolean
     */
    public boolean canConverter() {
        if (getWcBank() != null) {
            if (getWcBank().wcFree()) {
                return true;
            }
        }
        return false;
    }
//------------------------------------------------------------------------------
    /**
     * Retorna true se for wcr total; falso caso contrário;
     * @return boolean wcrTotal
     */
    public boolean isWcrTotal() {
        return wcrTotal;
    }
//------------------------------------------------------------------------------
    /**
     *Configura se é wcr total
     *@param wcrTotal boolean
     */
    public void setWcrTotal(boolean wcrTotal) {
        this.wcrTotal = wcrTotal;
        
        if (wcrTotal==true){//conversão total. Configurando o numero ideal de conversores.
            int numWc = 0;
            Vector<Link> interfaces = this.oxc.getLinksList();
            for (int j = 0; j < interfaces.size(); j++) {
                numWc += interfaces.get(j).getNumWave();
            }
            this.setWcr(numWc);
        }
        
    }
    
    
    
    
}
