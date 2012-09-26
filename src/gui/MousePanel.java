package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import network.Link;
import network.Oxc;
import org.jdesktop.swingx.JXMapViewer;

/**
 *
 * <p>Title: Mouse Panel</p>
 * <p>Description: Esta classe é destinada ao Panel responsavel pela parte gráfica da modelagem da tiopologia;</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class MousePanel extends JPanel implements MouseMotionListener {    
    private Image oxcImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/gui/images/oxc.gif"));
    private Image oxcSelectedImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/gui/images/oxcSelected.gif"));
    private Topologia topologia;
    private NoGrf noGrf;
    private Vector jLVector = new Vector();
    
    private int actionButton;
    private static final int SQUARELENGTH = 20;
    private static final int MAXNSQUARES = 500;
    private Point[] squares = new Point[MAXNSQUARES];
    private int nsquares = 1;
    private int nenlace = 1;
    private NoGrf currentNo, current1 = null, current2 = null;
    private boolean inicio = true, verificaJEnl = false;
    private int elemento = 0;
    private int label = 0;
    private int cliqAtual=0;
    private MouseAdapter mouseAdapter;
    private boolean activated = false;
    /*int nLambda;*/
    private JXMapViewer mapa;
    private int lastMapZoom;
    private double lastMapX;
    private double lastMapY;
    private boolean mapLocked = true;
     

    public void paint(Graphics g, int a, int b, NoGrf i) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.black);
        if (i == currentNo) {
            g2.setPaint(Color.red);
            g2.drawString("Nó " + i.getName(), (a - 10), (b - 15));
            g2.drawImage(oxcSelectedImage, a - SQUARELENGTH / 2, b - SQUARELENGTH / 2, this);
        } else {            
            g2.drawString("Nó " + i.getName(), (a - 10), (b - 15));
            g2.drawImage(oxcImage, a - SQUARELENGTH / 2, b - SQUARELENGTH / 2, this);
        }
    }
    
//----------------------------------------------------------------------------------    
    public MousePanel(final Main main) {                
        topologia = new Topologia();

        mouseAdapter = new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                cliqAtual=evt.getButton();
                int x = evt.getX();
                int y = evt.getY();
                currentNo = findNo(x, y);

                if((currentNo!=null) && (evt.getButton()==evt.BUTTON3)){
                    main.showPropriedades(currentNo);
                }

                LinkGrf currentLink = null;
                int tmpId = findEnl(x,y);
                if (tmpId !=-1){
                    currentLink = topologia.procuraLink(tmpId);
                }
                if((currentLink!=null) && (evt.getButton()==evt.BUTTON3)){
                    main.showPropriedades(topologia, topologia.getListaLink().indexOf(currentLink),currentLink);
                }

                mousePrecionado(evt);
                repaint();

                if ( (evt.getButton() == evt.BUTTON1) && (actionButton == 4)) {
                    if (currentNo != null) {
                        remove(currentNo);
                        current1 = current2 = null;
                    }
                }

                if ( (evt.getButton() == evt.BUTTON1) && (actionButton == 3)) {
                    if (currentNo == null) {
                        addNo(x, y);
                    }
                }
                if ( (evt.getButton() == evt.BUTTON1) &&
                        ( (actionButton == 2) || (actionButton == 4))) {
                    if (currentNo != null) { // inside a square

                        if ( (current1 == null) && (current2 == null)) {
                            current1 = findNo(x, y);
                        } else {
                            if (current1 != null) {
                                //Remove seleção do nó caso seja clicado uma segunda vez
                                if(current1 == currentNo){
                                    current1 = currentNo = null;
                                    return;
                                }
                                current2 = findNo(x, y);
                                if (current1 != current2) {
                                    if (topologia.existeLink(current1, current2) == -1) {
                                        addEnl(current1, current2, 1, 40, true);
                                        LinkConfig lc = new LinkConfig(topologia,topologia.getListaLink().size()-1);
                                        //lc.setSize(120,160);
                                        lc.setLocation((current1.getX()+current2.getX())/2,(current1.getY()+current2.getY())/2);
                                        lc.setVisible(true);

                                    }
                                    //current1 = current2 = null; //Modificado para permitir a construção dos enlaces em sequência
                                    current1 = current2;
                                    current2 = null;
                                }
                            }
                        }
                    }

                    else {
                        current1 = current2 = null;
                    }
                }
            }

        };

        if(activated){
            addMouseListener(mouseAdapter);
            addMouseMotionListener(this);
        }
    }

    public void switchPanel(){

        if(activated)
            deactivatePanel();
        else
            activatePanel();

    }

    public void activatePanel(){

        activated = true;
        addMouseListener(mouseAdapter);
        addMouseMotionListener(this);
    }

    public void deactivatePanel(){

        activated = false;
        removeMouseListener(mouseAdapter);
        removeMouseMotionListener(this);
    }
    
    public MousePanel getTopologia() {
        return this;
    }
    
    public Vector getTopologiaListaNo() {
        return this.topologia.getListaNo();
    }
    
    public Vector getTopologiaListaEnl() {
        return this.topologia.getListaLink();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
     
        g.setColor(new Color(100,100,100,100));

        if(lastMapZoom != mapa.getZoom()){

            double x_m = mapa.getSize().getWidth()/2;
            double y_m = mapa.getSize().getHeight()/2;
            for (int i = 0; i < topologia.getListaNo().size(); i++) {
                NoGrf no = ((NoGrf)topologia.getListaNo().get(i));
                no.setX((int)(x_m + (no.getX() - x_m)*Math.pow(2, lastMapZoom - mapa.getZoom())));
                no.setY((int)(y_m + (no.getY() - y_m)*Math.pow(2, lastMapZoom - mapa.getZoom())));

            }
            for (int x = 0; x < topologia.getListaLink().size(); x++) {
                LinkGrf link = (LinkGrf) topologia.getListaLink().get(x);
                link.moveLabel();
            }
            lastMapZoom = mapa.getZoom();
            
        }
        else
        if(mapLocked && (lastMapX != mapa.getCenter().getX() || lastMapY != mapa.getCenter().getY())){

            double x_d = mapa.getCenter().getX() - lastMapX;
            double y_d = mapa.getCenter().getY() - lastMapY;
            for (int i = 0; i < topologia.getListaNo().size(); i++) {
                NoGrf no = ((NoGrf)topologia.getListaNo().get(i));
                no.setX((int)(no.getX() - x_d));
                no.setY((int)(no.getY() - y_d));

            }
            for (int x = 0; x < topologia.getListaLink().size(); x++) {
                LinkGrf link = (LinkGrf) topologia.getListaLink().get(x);
                link.moveLabel();
            }
        }
        lastMapX = mapa.getCenter().getX();
        lastMapY = mapa.getCenter().getY();
        for (int i = 0; i < topologia.getListaNo().size(); i++) {
            drawNo(g, ( (NoGrf) topologia.getListaNo().get(i)));
            
        }
        for (int x = 0; x < topologia.getListaLink().size(); x++) {
            drawEnl(g, ( (LinkGrf) topologia.getListaLink().get(x)));
        }
        
    }
    
    public NoGrf findNo(int x, int y) {
        NoGrf a;
        for (int i = 0; i <= topologia.getListaNo().size() - 1; i++) {
            a = (NoGrf) topologia.getNo(i);
            if ( (a.getX() - SQUARELENGTH / 2 <= x) &&
                    (x <= a.getX() + SQUARELENGTH / 2) &&
                    (a.getY() - SQUARELENGTH / 2 <= y) &&
                    (y <= a.getY() + SQUARELENGTH / 2)) {
                
                return (a);
            }
        }
        
        return null;
    }
    
    public void Reset() {
        currentNo = null;
        current2 = null;
        current1 = null;
        LinkGrf aux;
        for (int i = 0; i <= topologia.getListaLink().size() - 1; i++) {
            aux = (LinkGrf) topologia.getLink(i);
            aux.apagaLabel();
        }
        topologia.getListaLink().removeAllElements();
        topologia.getListaNo().removeAllElements();
        nsquares = 1;
        nenlace = 1;
        repaint();        
    }
    
    /**
     * Esse metodo e responsavel por contruir funcoes de primeiro grau entre todos os pares de nos que existem.
     * E a todo momento que o mouse e movimentado, ele pega as cordenadas do mouse e verifica se esse par de pontos faz parte de alguma das funcoes de primeiro grau.
     * @param x = coordenada do eixo x (evento do mouse).
     * @param y = coordenada do eixo y (evento do mouse).
     */
    public int findEnl(int x, int y) {
        double c, a, b, x1, x2, y1, y2;
        LinkGrf aux;
        for (int i = 0; i < topologia.getListaLink().size(); i++) {
            aux = (LinkGrf) topologia.getLink(i);
            
            x1 = aux.getX1();
            x2 = aux.getX2();
            y1 = aux.getY1();
            y2 = aux.getY2();
            
            a = (y1 - y2) / (x1 - x2);
            b = y1 - (a * x1);
            if ( (x <= Math.max(aux.getX1(), aux.getX2())) &&
                    (x >= Math.min(aux.getX1(), aux.getX2())) &&
                    (y <= Math.max(aux.getY1(), aux.getY2())) &&
                    (y >= Math.min(aux.getY1(), aux.getY2()))) {
                if (Double.isInfinite(a) == true) {
                    c = 0;
                    if ( (x >= x1 - 2) && (x <= x1 + 2)) {
                        c = y;
                    }
                } else {
                    c = (x * a) + b;
                }
                
                if ( (c <= y + 2 && c >= y - 2)) {
                    return aux.getId();
                }
            }
        }
        
        for (int i = 0; i < topologia.getListaLink().size(); i++) {
            aux = (LinkGrf) topologia.getLink(i);
            boolean p = aux.verificaPos(x, y);
            if (p == true) {
                return aux.getId();
            }
        }
        return -1;
    }
    
    public void drawNo(Graphics g, NoGrf i) {
        
        if (i != null) {
            int a, b;
            a = i.getX();
            b = i.getY();
            paint(g, a, b, i);
        }
    }
    
    public void drawEnl(Graphics g, LinkGrf e) {
        Graphics2D g2 = (Graphics2D) g;
        Color cor = e.getCor();
        g.setColor(cor);                
        g.drawLine(e.getNoOrigem().getX(),
                e.getNoOrigem().getY(),
                e.getNoDestino().getX(),
                e.getNoDestino().getY()
                );

        e.calculateDistance();
        add(e.getLabel());
    }

   
    
    public void addNo(int x, int y) {
        if (topologia.getListaNo().size() < MAXNSQUARES) {
            currentNo = new NoGrf(new Point(x, y),nsquares+"");
            current1 = null; //remove possível seleção deixada anteriormente na construção sequencial de enlaces
            topologia.adicionarNo(currentNo);
            nsquares++;
            repaint();
        }
    }
    
    public void addNo(int x, int y, int id) {
        if (topologia.getListaNo().size() < MAXNSQUARES) {
            currentNo = new NoGrf(new Point(x, y),id - 1+"");            
            topologia.adicionarNo(currentNo);
            nsquares++;
            repaint();
        }
    }
    
    public void updateNsquares(){
    	
    	int id = 0;
    	for(Object o : this.getTopologiaListaNo()){
    		
    		NoGrf no = (NoGrf) o;
    		if(id < Integer.parseInt(no.getName()))
    				id = Integer.parseInt(no.getName());
    		
    	}
    	//seta nsquares para maior valor de id do nó encontrado mais 1
    	nsquares = id + 1;
    }
    
    
    public void addEnl(NoGrf n1, NoGrf n2,int cost, int numWave, boolean bidirecional) {
        LinkGrf aux = new LinkGrf(nenlace, n1, n2, cost, numWave, bidirecional, mapa);
      
        topologia.adicionarLink(aux);
        nenlace++;
        repaint();
        n1.getOxc().addLink(n2.getOxc(),cost,numWave);
        n2.getOxc().addLink(n1.getOxc(),cost,numWave);        
    }
/*  public void addEnl(NoGrf nn1, NoGrf nn2, int id) {
 
    topologia.adicionarEnl(new LinkGrf(id, nn1, nn2));
    nenlace++;
    repaint();
    nn1.adicionar_interface(nn2);
    //nn2.adicionar_interface(nn1);
 
  }*/
    public void removeNo(int noId){
        NoGrf aux;
        aux=this.topologia.getNo(noId);
        this.remove(aux);
    }
    
    public void remove(NoGrf n) {
        LinkGrf aux;
        int nEnlRemove = 0;
        if (n == null) {
            return;
        }
        //Armazena na variavel nEnlRemove quantos enlaces serao removidos
        for (int i = 0; i <= topologia.getListaLink().size() - 1; i++) {
            aux = (LinkGrf) topologia.getLink(i);
            if ( (aux.getNoOrigem() == n) || (aux.getNoDestino() == n)) {
                nEnlRemove++;
            }
        }
        
        for (int j = 1; j <= nEnlRemove; j++) {
            for (int i = 0; i <= topologia.getListaLink().size() - 1; i++) {
                aux = (LinkGrf) topologia.getLink(i);
                if ( (aux.getNoOrigem() == n) || (aux.getNoDestino() == n)) {
                    topologia.removeLink(aux);
                    //aux.RemoveLabel();
                    this.remove(aux.getLabel());
                    repaint();
                    revalidate();
                    break;
                }
            }
        }
        topologia.removeNo(n);
        repaint();
    }
    
    
    
    public void mousePrecionado(MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        if (actionButton == 4&&evt.getButton()==evt.BUTTON1) {
            LinkGrf testeEnl;
            int i = findEnl(x, y);
            if (i != -1) {
                testeEnl = (LinkGrf) topologia.procuraLink(i);
                topologia.removeLink(testeEnl);
                this.remove(testeEnl.getLabel());
                revalidate();
            }
            NoGrf testeNo;
            testeNo = findNo(x, y);
            if (testeNo != null) {
                currentNo = testeNo;
            }
            repaint();
        }
    }
    
    public void mouseMoved(MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        if (actionButton == 4) {
            LinkGrf testeEnl;
            int i = findEnl(x, y);
            if (i != -1) {
                for (int j = 0; j <= topologia.getListaLink().size() - 1; j++) {
                    if (j != i) {
                        testeEnl = ( (LinkGrf) topologia.getLink(j));
                        testeEnl.returnColor();
                        testeEnl.defineCor(false);
                    }
                }
                testeEnl = topologia.procuraLink(i);
                testeEnl.changeLColor();
                testeEnl.defineCor(true);
            } else {
                for (int j = 0; j <= topologia.getListaLink().size() - 1; j++) {
                    testeEnl = ( (LinkGrf) topologia.getLink(j));
                    testeEnl.returnColor();
                    testeEnl.defineCor(false);
                }
            }
            NoGrf testeNo;
            testeNo = findNo(x, y);
            if (testeNo != null) {
                currentNo = testeNo;
                for (int j = 0; j <= topologia.getListaLink().size() - 1; j++) {
                    testeEnl = (LinkGrf) topologia.getLink(j);
                    if ( (testeEnl.getNoDestino().getName().equalsIgnoreCase(currentNo.getName())) ||
                            (testeEnl.getNoOrigem().getName().equalsIgnoreCase(currentNo.getName()))) {
                        testeEnl.changeLColor();
                        testeEnl.defineCor(true);
                    }
                }
                
            }
            repaint();
        }
        
        setCursor(Cursor.getDefaultCursor());
    }
    
    public void imprimeListaNo() {
        if (topologia.getListaNo().size() > 0) {
            for (int i = 0; i <= topologia.getListaNo().size() - 1; i++) {
                System.out.println("Posicao:" + i + " No id=" +
                        ( (NoGrf) topologia.getListaNo().get(i)).
                        getName());
            }
        }
        System.out.println("-----------------------------");
    }
    
    public void imprimeListaEnl() {
        LinkGrf aux;
        if (topologia.getListaLink().size() > 0) {
            for (int i = 0; i <= topologia.getListaLink().size() - 1; i++) {
                aux = ( (LinkGrf) topologia.getLink(i));
                System.out.print("Posicao:" + i + " Enl id=" + aux.getId() + "   ");
                System.out.println("Ori:" + aux.getNoOrigem().getName() + " Dest:" +
                        aux.getNoDestino().getName());
            }
        }
        System.out.println("-----------------------------");
        
    }
    
    public void imprimeLinks(){        
        for (int i = 0; i < topologia.getListaNo().size(); i++) {
            Oxc tmp = topologia.getNo(i).getOxc();
            for (int j = 0; j < tmp.getNumInterfaces(); j++) {
                Link l = tmp.getLinksList().get(j);
                System.out.println(l.getName()+";"+l.getCost()+";"+l.getNumWave());
            }
        }
    }
    
    public void actionButton(int a) {
        actionButton = a;
        
    }
    
    public void mouseDragged(MouseEvent evt) {
        
        if ((actionButton == 0 ||actionButton == 1)&&cliqAtual==1) {
            int x = evt.getX();
            int y = evt.getY();
            NoGrf atual;
            Graphics g;
            
            if (currentNo != null) {
                g = getGraphics();
                g.setXORMode(getBackground());
                drawNo(g, currentNo);
                LinkGrf auxEnl;
                for (int i = 0; i <= topologia.getListaLink().size() - 1; i++) {
                    auxEnl = (LinkGrf) topologia.getLink(i);
                    if ( (auxEnl.getNoDestino().getName().equalsIgnoreCase(currentNo.getName())) ||
                            (auxEnl.getNoOrigem().getName().equalsIgnoreCase(currentNo.getName()))) {
                        g = getGraphics();
                        g.setXORMode(getBackground());
                        auxEnl.moveLabel();
                        drawEnl(g, auxEnl);
                    }
                }
                currentNo.setX(x);
                currentNo.setY(y);
                drawNo(g, currentNo);
                for (int i = 0; i < topologia.getListaLink().size() - 1; i++) {
                    auxEnl = (LinkGrf) topologia.getLink(i);
                    
                    if ( (auxEnl.getNoDestino() == currentNo) &&
                            (auxEnl.getNoOrigem() == currentNo)) {
                        drawEnl(g, auxEnl);
                    }
                }
                g.dispose();
                repaint();
            }
        }
    }

    void setMapa(JXMapViewer mainMap) {

        this.mapa = mainMap;
        resetMap();
    }
    
    void resetMap(){
        this.lastMapX = mapa.getCenter().getX();
        this.lastMapY = mapa.getCenter().getY();
        this.lastMapZoom = mapa.getZoom();
    }

    void switchMapLock() {

        mapLocked = !mapLocked;
    }
    
    void setMapLock(boolean lock){
        mapLocked = lock;
    }
    
    
}
