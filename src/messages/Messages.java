/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import java.util.Properties;

/**
 *
 * @author Danilo
 */
public class Messages {

    Properties msg;

    public Messages(String file) {

        msg = Bundle.loadProperties(this.getClass().getPackage().getName() + "/" + file);
    }

    public  String recuperarMsg(String key){

        return (String)msg.get(key);
    }

}
