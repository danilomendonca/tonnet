/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 *
 * @author seadev
 */
public class FloatJSlider extends JSlider {

    @Override
    public Dictionary getLabelTable() {
        return super.getLabelTable();
    }

 private static final long serialVersionUID = 1L;
        static final float FLOAT_MINIMUM = 0.0f;
        static final float FLOAT_MAXIMUM = 100.0f;
        static final float FLOAT_MIDDLE = 50.0f;
        static final int PRECISION_MULTIPLIER  = 100;

        public FloatJSlider(){
                super();
                setFloatMinimum(FLOAT_MINIMUM);
                setFloatMaximum(FLOAT_MAXIMUM);
                setFloatValue(FLOAT_MIDDLE);
                updateLabelTable();
        }

        public FloatJSlider(float min, float max, float val){
                super();
                setFloatMinimum(min);
                setFloatMaximum(max);
                setFloatValue(val);
                updateLabelTable();
                
        }
        
        private void updateLabelTable(){
            
            Hashtable <Integer, JLabel> labelTable = new Hashtable <Integer, JLabel> ();
            for(Integer i = super.getMinimum(); i <= super.getMaximum(); i++){
                labelTable.put( i, new JLabel(i/PRECISION_MULTIPLIER + "") );
            }
            this.setLabelTable( labelTable );
        }

        public float getFloatMaximum() {
                return( getMaximum()/FLOAT_MAXIMUM );
        }

        public float getFloatMinimum() {
                return( getMinimum()/FLOAT_MAXIMUM );
        }

        public float getFloatValue() {
                return( getValue()/FLOAT_MAXIMUM );
        }

        public void setFloatMaximum(float max) {
                setMaximum((int)(max*PRECISION_MULTIPLIER));
        }

        public void setFloatMinimum(float min) {
                setMinimum((int)(min*PRECISION_MULTIPLIER));
        }

        public void setFloatValue(float val) {
                setValue((int)(val*PRECISION_MULTIPLIER));
                setToolTipText(Float.toString(val));
        }
}