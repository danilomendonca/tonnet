/*
 * LinkConfig.java
 *
 * Created on July 30, 2007, 3:29 PM
 */

package gui;

/**
 *
 * @author  Gil
 */
public class LinkConfig extends javax.swing.JFrame {
    
    /** Creates new form LinkConfig */
    public LinkConfig(Topologia topologia, int indexOfLink) {
        initComponents();
        initOtherComponents(topologia,indexOfLink);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jRadioButtonBidirecional = new javax.swing.JRadioButton();
        jLabelNumWave = new javax.swing.JLabel();
        jSpinnerNumWave = new javax.swing.JSpinner();
        jLabelCusto = new javax.swing.JLabel();
        jSpinnerCusto = new javax.swing.JSpinner();
        jSpinnerDistancia = new javax.swing.JSpinner();
        jLabelDistancia = new javax.swing.JLabel();

        setTitle("Configura��es Link");
        setBackground(new java.awt.Color(255, 255, 255));
        setFocusableWindowState(false);
        setMinimumSize(new java.awt.Dimension(135, 144));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(135, 144));

        jButtonOk.setMnemonic('o');
        jButtonOk.setText("Aplicar");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jRadioButtonBidirecional.setSelected(true);
        jRadioButtonBidirecional.setText("Bidirecional");
        jRadioButtonBidirecional.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonBidirecional.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonBidirecional.setOpaque(false);

        jLabelNumWave.setText("W:");
        jLabelNumWave.setToolTipText("N�mero de Comprimentos de Onda");

        jSpinnerNumWave.setToolTipText("N�mero de Comprimentos de Onda");

        jLabelCusto.setText("Custo:");

        jLabelDistancia.setText("Dist�ncia:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(15, 15, 15)
                .add(jLabelCusto)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSpinnerCusto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelNumWave)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRadioButtonBidirecional)
                    .add(jButtonOk)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jSpinnerNumWave, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabelDistancia)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerDistancia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jSpinnerCusto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelNumWave)
                    .add(jLabelCusto)
                    .add(jSpinnerNumWave, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDistancia)
                    .add(jSpinnerDistancia, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRadioButtonBidirecional)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonOk)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 277, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        this.topologia.setLinkBidirecional(indexOfLink,jRadioButtonBidirecional.isSelected());        
        this.topologia.setNumWaveOfLink(indexOfLink,(Integer)jSpinnerNumWave.getValue());
        this.topologia.setCostOfLink(indexOfLink,new Double((Integer)jSpinnerCusto.getValue()));
        //distancia
        this.setVisible(false);        
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void initOtherComponents(Topologia topologia, int indexOfLink) {
        this.topologia=topologia;
        this.indexOfLink = indexOfLink;        
        this.jSpinnerCusto.setValue(1);
        this.jSpinnerNumWave.setValue(40);
        this.jSpinnerDistancia.setValue(1);
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabelCusto;
    private javax.swing.JLabel jLabelDistancia;
    private javax.swing.JLabel jLabelNumWave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonBidirecional;
    private javax.swing.JSpinner jSpinnerCusto;
    private javax.swing.JSpinner jSpinnerDistancia;
    private javax.swing.JSpinner jSpinnerNumWave;
    // End of variables declaration//GEN-END:variables

    private Topologia topologia;
    private int indexOfLink;
    
}