
package gui;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PropriedadesNo extends javax.swing.JFrame {
    
    /** Creates new form PropriedadesNo */
    public PropriedadesNo(NoGrf no) {
        initComponents();
        initOtherComponents(no);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelWCRexp = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelWCRexp3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelNome = new javax.swing.JLabel();
        jLabelWCR = new javax.swing.JLabel();
        jLabelWCRTotal = new javax.swing.JLabel();
        jLabelNumConversores = new javax.swing.JLabel();
        jLabelTx = new javax.swing.JLabel();
        jLabelRx = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jComboBoxWCR = new javax.swing.JComboBox();
        jComboBoxWCRTotal = new javax.swing.JComboBox();
        jSpinnerNConversores = new javax.swing.JSpinner();
        jComboBoxTx = new javax.swing.JComboBox();
        jComboBoxRx = new javax.swing.JComboBox();
        jButtonAplicar = new javax.swing.JButton();
        jButtonFechar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaNoProp = new javax.swing.JTextArea();
        jLabelPeso = new javax.swing.JLabel();
        jSpinnerPeso = new javax.swing.JSpinner();

        setTitle("Propriedades do N�");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(297, 381));
        setResizable(false);

        jLabelWCRexp.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jLabelWCRexp3.setText("jLabel1");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabelNome.setText("Nome :");

        jLabelWCR.setText("WCR :");

        jLabelWCRTotal.setText("WCR TOTAL :");
        jLabelWCRTotal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabelNumConversores.setText("WCs :");

        jLabelTx.setText("Tx :");

        jLabelRx.setText("Rx :");

        jComboBoxWCR.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "false", "true" }));
        jComboBoxWCR.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxWCRItemStateChanged(evt);
            }
        });

        jComboBoxWCRTotal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "false", "true" }));
        jComboBoxWCRTotal.setEnabled(false);
        jComboBoxWCRTotal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxWCRTotalItemStateChanged(evt);
            }
        });

        jSpinnerNConversores.setEditor(jSpinnerNConversores.getEditor());
        jSpinnerNConversores.setEnabled(false);

        jComboBoxTx.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ilimitado" }));
        jComboBoxTx.setEnabled(false);

        jComboBoxRx.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ilimitado" }));
        jComboBoxRx.setEnabled(false);

        jButtonAplicar.setText("Aplicar");
        jButtonAplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAplicarActionPerformed(evt);
            }
        });

        jButtonFechar.setText("Fechar");
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        jTextAreaNoProp.setColumns(20);
        jTextAreaNoProp.setEditable(false);
        jTextAreaNoProp.setFont(new java.awt.Font("Courier", 0, 11)); // NOI18N
        jTextAreaNoProp.setLineWrap(true);
        jTextAreaNoProp.setRows(3);
        jTextAreaNoProp.setTabSize(4);
        jTextAreaNoProp.setText("WCR = N� com Capacidade de Convers�o de Comprimento de Onda. WCR TOTAL = N� com Capacidade de Convers�o Total. WCs: N�mero de Conversores de Comprimento de Onda. Tx = Transmissores. Rx = Receptores.");
        jTextAreaNoProp.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaNoProp);

        jLabelPeso.setText("Peso :");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(37, 37, 37)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabelWCRTotal)
                            .add(jLabelWCR)
                            .add(jLabelNumConversores)
                            .add(jLabelNome))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldNome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                            .add(jSpinnerNConversores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                            .add(jComboBoxWCRTotal, 0, 212, Short.MAX_VALUE)
                            .add(jComboBoxWCR, 0, 212, Short.MAX_VALUE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabelTx)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxTx, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelRx)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxRx, 0, 203, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(93, 93, 93)
                        .add(jLabelPeso)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerPeso, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jButtonAplicar)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonFechar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelNome))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelWCR)
                    .add(jComboBoxWCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jComboBoxWCRTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelWCRTotal))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelNumConversores)
                    .add(jSpinnerNConversores, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTx)
                    .add(jComboBoxTx, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelRx)
                    .add(jComboBoxRx, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jSpinnerPeso, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelPeso))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonAplicar)
                    .add(jButtonFechar))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxWCRItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxWCRItemStateChanged
        if (this.jComboBoxWCR.getSelectedItem().toString().equalsIgnoreCase("false")){
            this.jComboBoxWCRTotal.setEnabled(false);
            this.jSpinnerNConversores.setEnabled(false);
        }else{            
            this.jComboBoxWCRTotal.setEnabled(true);
            this.jSpinnerNConversores.setEnabled(true);            
        }

    }//GEN-LAST:event_jComboBoxWCRItemStateChanged

    private void jComboBoxWCRTotalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxWCRTotalItemStateChanged
        if (this.jComboBoxWCRTotal.getSelectedItem().toString().equalsIgnoreCase("false")){            
            this.jSpinnerNConversores.setEnabled(true);
        }else{            
            this.jSpinnerNConversores.setEnabled(false);            
        }
    }//GEN-LAST:event_jComboBoxWCRTotalItemStateChanged

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonFecharActionPerformed

    private void jButtonAplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAplicarActionPerformed
        this.no.setName(this.jTextFieldNome.getText());//nome
        
        if (this.jComboBoxWCR.getSelectedIndex()==1){//wcr true
            if (this.jComboBoxWCRTotal.getSelectedItem().toString().equalsIgnoreCase("true")){
                this.no.setWcrTotal(true);
            }else{//wcr parcial         
                //num conversores
                this.no.setWcr(Integer.parseInt(this.jSpinnerNConversores.getModel().getValue().toString()));    
                this.no.setWcrTotal(false);
            }
        }else{//wcr false
            this.no.setWcr(0);
            this.no.setWcrTotal(false);
        }
        this.no.setPrivilege(Integer.parseInt(this.jSpinnerPeso.getModel().getValue().toString()));
        this.setVisible(false);
    }//GEN-LAST:event_jButtonAplicarActionPerformed
    
    private void initOtherComponents(NoGrf no) {
        this.no = no;        
        this.setTitle("N� "+no.getName()+" - Propriedades");
        this.jTextFieldNome.setText(no.getName());
        
        ComboBoxModel cbmWCR = this.jComboBoxWCR.getModel();
        ComboBoxModel cbmWCRTotal = this.jComboBoxWCRTotal.getModel();
        if (this.no.isWCR()){//WCR true            
            cbmWCR.setSelectedItem(cbmWCR.getElementAt(1));//wcr true.            
            this.jComboBoxWCRTotal.setEnabled(true);
            if (this.no.isWcrTotal()){//wcr total     
                cbmWCRTotal.setSelectedItem(cbmWCRTotal.getElementAt(1));//wcr total true
                this.jSpinnerNConversores.setEnabled(false);
            }else{//� wcr parcial.
                cbmWCRTotal.setSelectedItem(cbmWCRTotal.getElementAt(0));//wcr total false
                this.jSpinnerNConversores.setEnabled(true);
                this.jSpinnerNConversores.setValue(no.getWcBank().getNumWcs());    
            }            
        }else{//WCR false
            cbmWCR.setSelectedItem(cbmWCR.getElementAt(0));//false.
            this.jComboBoxWCRTotal.setEnabled(false);
            this.jSpinnerNConversores.setEnabled(false);
        }
        this.jSpinnerPeso.setValue(no.getPrivilege());     
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAplicar;
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JComboBox jComboBoxRx;
    private javax.swing.JComboBox jComboBoxTx;
    private javax.swing.JComboBox jComboBoxWCR;
    private javax.swing.JComboBox jComboBoxWCRTotal;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelNumConversores;
    private javax.swing.JLabel jLabelPeso;
    private javax.swing.JLabel jLabelRx;
    private javax.swing.JLabel jLabelTx;
    private javax.swing.JLabel jLabelWCR;
    private javax.swing.JLabel jLabelWCRTotal;
    private javax.swing.JLabel jLabelWCRexp;
    private javax.swing.JLabel jLabelWCRexp3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerNConversores;
    private javax.swing.JSpinner jSpinnerPeso;
    private javax.swing.JTextArea jTextAreaNoProp;
    private javax.swing.JTextField jTextFieldNome;
    // End of variables declaration//GEN-END:variables
    private NoGrf no;
}
