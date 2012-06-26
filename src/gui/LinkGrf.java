package gui;

import java.awt.*;
import javax.swing.*;
import org.jdesktop.swingx.JXMapViewer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LinkGrf extends network.Link {
    private int identificador;
    private NoGrf noOrigem;
    private NoGrf noDestino;
    private JLabel jLabelLink;
    private Color cor = Color.BLUE;
    private JXMapViewer mapa;
    private boolean bidirecional;
    
    public LinkGrf(int id, NoGrf n1, NoGrf n2,double cost, int numWave, boolean bidirecional, JXMapViewer mapa ) {
        super(n1.getOxc(),n2.getOxc(),cost,numWave);
        identificador = id;
        noOrigem = n1;
        noDestino = n2;
        jLabelLink = new JLabel();
        jLabelLink.setBounds(new Rectangle(130, 35, 165, 15));
        jLabelLink.setLocation( ( (getNoOrigem().getX() + getNoDestino().getX()) / 2),
                ( (getNoOrigem().getY() + getNoDestino().getY()) / 2));
        this.setBidirecional(bidirecional);
        this.mapa = mapa;
    }
    
    public JLabel getLabel() {
        if (bidirecional){
            jLabelLink.setText("E.bi " + getName() + " " + getDistance() + "Km");
            jLabelLink.setForeground(Color.BLUE);
        }else{
            jLabelLink.setText("E " + getName() + " " + getDistance() + "Km");
            jLabelLink.setForeground(new Color(100,150,150));
        }
        // jLabelLink.setText("Link " + identificador);
        return jLabelLink;
    }

    public void calculateDistance(){

        if(mapa == null)
            return ;
        //2.212042220349313
        //2.283636363636364
        int x = (getNoOrigem().getX() - getNoDestino().getX());
        int y = (getNoOrigem().getY() - getNoDestino().getY());
        double corretor = 2.283636363636364 * Math.pow(2, mapa.getZoom() - 11);
        
        setDistance(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * corretor);
    }
    
    public void defineCor(boolean a) {
        if (a == false) {
            cor = Color.BLUE;
        } else {
            cor = Color.RED;
        }
    }
    
    public Color getCor() {
        return cor;
    }
    
    public void apagaLabel() {
        jLabelLink.setText("");
        jLabelLink.removeAll();
    }
    
    public void moveLabel() {
        jLabelLink.setLocation( ( (getNoOrigem().getX() + getNoDestino().getX()) / 2),
                ( (getNoOrigem().getY() + getNoDestino().getY()) / 2));
    }
    
    public void changeLColor() {
        jLabelLink.setForeground(Color.red);
    }
    
    public void returnColor() {
        jLabelLink.setForeground(Color.black);
    }
    
    public boolean verificaPos(int x, int y) {
        int i = jLabelLink.getX();
        int j = jLabelLink.getY();
        
        if ( (x >= (getNoOrigem().getX() + getNoDestino().getX()) / 2) &&
                (x <= ( (getNoOrigem().getX() + getNoDestino().getX()) / 2) + 37) &&
                (y >= ( (getNoOrigem().getY() + getNoDestino().getY()) / 2)) &&
                (y <= ( (getNoOrigem().getY() + getNoDestino().getY()) / 2) + 14))
            
        {
            return true;
        }
        return false;
    }
    
    //--------------------------------------------------------
    
    public NoGrf getNoOrigem() {
        return noOrigem;
    }
    
    //--------------------------------------------------------
    public NoGrf getNoDestino() {
        return noDestino;
    }
    
    //--------------------------------------------------------
    public int getId() {
        return identificador;
    }
    
    public int getJLAbelLinkX() {
        return jLabelLink.getX();
    }
    
    public int getJEnlY() {
        return identificador;
    }
    
//--------------------------------------------------------
    public int getX1() {
        return this.getNoOrigem().getX();
    }
    
//--------------------------------------------------------
    public int getX2() {
        return this.getNoDestino().getX();
    }
    
//--------------------------------------------------------
    public int getY1() {
        return this.getNoOrigem().getY();
    }
    
//--------------------------------------------------------
    public int getY2() {
        return this.getNoDestino().getY();
    }
//--------------------------------------------------------
    public void setBidirecional(boolean b) {
        if (b){
            jLabelLink.setForeground(Color.BLUE);
        }else{
            jLabelLink.setForeground(new Color(100,150,150));
        }
        this.bidirecional=b;
    }
//--------------------------------------------------------
    public boolean isBidirecional() {
        return this.bidirecional;
    }

    void setMapa(JXMapViewer mapa) {
        this.mapa = mapa;
    }
}
