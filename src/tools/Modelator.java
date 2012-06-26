/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.awt.Point;
import java.util.List;
import java.util.Vector;

/**
 * Classe para popular topologia com modelos conhecidos de redes.
 * @author Danilo
 */
public abstract class Modelator {

    protected int DEFAULT_PRIVILEGE;
    protected int MARGEM = 50;
    protected int x = 0, y = 0, c_x, c_y;

    public Modelator(int centro_x, int centro_y, int privilege) {

        this.DEFAULT_PRIVILEGE = privilege;
        this.c_x = centro_x;
        this.c_y = centro_y;
    }

    public abstract List[] populate();
    abstract Point position(int node);
    
    
}
