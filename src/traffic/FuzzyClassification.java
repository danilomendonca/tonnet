/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traffic;

import net.sourceforge.jFuzzyLogic.FIS;
import routing.RoutingControl;

/**
 *
 * @author seadev
 */
public class FuzzyClassification {   
    
    public static void main(String[] args) throws Exception {
       classifyTraffic(9, 0.7, 3);
    }
    
    public static char classifyTraffic(double lambda, double hurst, double interarrival){
         // Load from 'FCL' file
        String fileName = "src/traffic/classification.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load classification file: '" + fileName + "'");
            return RoutingControl.NONE;
        }

        // Show 
        //fis.chart();

        // Set inputs
        fis.setVariable("LAMBDA", lambda);
        fis.setVariable("HURST", hurst);
        fis.setVariable("INTERCHEGADAS", interarrival);
        
        // Evaluate
        fis.evaluate();

        // Show output variable's chart 
        double out = fis.getVariable("comutacao").getLatestDefuzzifiedValue();
        
        // Print ruleSet
        System.out.println(out);
        
        if(out <= 0.6)
            return RoutingControl.BURST;
        else
            return RoutingControl.CIRCUIT;
    }
    
}
