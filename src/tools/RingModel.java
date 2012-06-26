/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import gui.LinkGrf;
import gui.NoGrf;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import network.Node;

/**
 *
 * @author Danilo
 */
public class RingModel extends Modelator{

    private final int RAIO;
    private short dir = 1;


    public RingModel(int c_x, int c_y, int raio, int privilege) {
        super(c_x, c_y, privilege);
        this.RAIO = raio;
        x = c_x - RAIO;
    }

    Point position(int node){

        int x_last = x;
        
        if(x + dir * (2*RAIO)/4 > c_x + RAIO && dir == 1)
            dir = -1;

        x = x + dir * (2*RAIO)/4;
        
        y = (int)Math.sqrt(Math.pow(RAIO, 2) - Math.pow((x_last - c_x), 2))*dir + c_y;
       
        return new Point(x_last, y);
    }


    public List [] populate(){

        List<Node> listNode = new ArrayList<Node>();
        Hashtable<String, NoGrf> listNodeGrf = new Hashtable<String, NoGrf>();
        List<LinkGrf> listLinkGrf = new ArrayList<LinkGrf>();
        Hashtable<String,Node> list = new Hashtable<String,Node>();//para 'pegar' um Node pelo nome
        short nodeName = 1;

        while(nodeName <= 8) {

            Node tmp = new Node(nodeName + "");
            tmp.setPrivilege(DEFAULT_PRIVILEGE);
            list.put(tmp.getName(),tmp);
            listNode.add(tmp);

            /*if (conversionType.get(0)==2){
              int numWCs = Integer.valueOf(line.nextToken());
              if (numWCs > 0)
                tmp.setWcr(numWCs);
            }else
                line.nextToken();//passa a linha*/

            NoGrf tmpGrf = new NoGrf(position(nodeName), tmp.getName());
            listNodeGrf.put(tmpGrf.getName(), tmpGrf);



            int linkId = 0;
            if(nodeName - 1 > 0){
                
                Node last = list.get(nodeName - 1 + "");
                NoGrf lastGrf = listNodeGrf.get(nodeName - 1 + "");
                last.getOxc().addLink(tmp.getOxc(), 1.0, 40); //CUSTO, NUM LAMBDAS
                LinkGrf lGrf = new LinkGrf(linkId++, lastGrf, tmpGrf, 1.0, 40, true, null); //CUSTO, NUM LAMBDAS, bidirecional
                lastGrf.getOxc().addLink(tmpGrf.getOxc(), 1.0, 40);
                tmpGrf.getOxc().addLink(lastGrf.getOxc(), 1.0, 40);
                listLinkGrf.add(lGrf);
            }

            nodeName++;
         }
       
        return new List[]{listNode, new Vector(listNodeGrf.values()), listLinkGrf};
    }

   

}
