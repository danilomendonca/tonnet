
package gui;

import java.util.Vector;
import javax.swing.JOptionPane;
import network.Mesh;
import root.NodeFileController;
import root.SimulationFileController;
import simulator.Simulation;
import simulator.SimulationManagement;
import simulator.Validate;

public class JanelaValidacao extends javax.swing.JFrame implements Runnable {
    
   public JanelaValidacao() {
        initComponents();
        this.setTitle("Validação");
        this.jProgressBar.setMinimum(0);
        this.jProgressBar.setMaximum(100); 
        this.jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        jPanel = new javax.swing.JPanel();
        jButton = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar();

        jTextArea.setColumns(20);
        jTextArea.setEditable(false);
        jTextArea.setRows(5);
        jScrollPane.setViewportView(jTextArea);

        jButton.setText("Parar");
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });

        jProgressBar.setForeground(new java.awt.Color(50, 150, 150));

        org.jdesktop.layout.GroupLayout jPanelLayout = new org.jdesktop.layout.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelLayout.createSequentialGroup()
                .add(87, 87, 87)
                .add(jButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(85, 85, 85))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonActionPerformed
        
    public void println(String text){
        this.jTextArea.append(text+"\n");
    }

    public void run() {        
       println("Validando...");
      try {
        //armazena os nomes principais dos arquivos de validacao
        Vector<String> files = Validate.getFiles();
        //percorre a lista de arquivos e inicia as respectivas simulacoes e validacoes
        for (int i = 0; i < files.size(); i++) {
          println("######### Etapa " + (i + 1) + " de " + files.size() +
                             " #########");
          String validateArgs[] = new String[4];
          validateArgs[0] = "validate/" + files.get(i);
          //executa a simulacao de validacao i
          this.simulationExecute(validateArgs);
          //compara os resultados da simulacao i com os respectivos resultados "validos"
          boolean valid = Validate.validate("files/validate/" + files.get(i) +
                                            "/resultsOk.res",
                                            "files/validate/" + files.get(i) +
                                            "/results.res");
          println("Resultado : " + valid);
          println("------------------------------------------------");
        }
         JOptionPane.showMessageDialog(null,"Validação Concluída!","fim",JOptionPane.INFORMATION_MESSAGE);       
      }
      catch (Exception ex) {
        ex.printStackTrace();
        System.err.println("Validate Error");
      }
    }
    
    private void simulationExecute(String[] args){
      String net = "files/" + args[0] + "/network.net";
      String sim = "files/" + args[0] + "/simulation.sim";
      String res = "files/" + args[0] + "/results.res";
      String pairs = "files/" + args[0] +"/pairs.prs";

      Vector config = new Vector(3);
      Simulation simulacao = SimulationFileController.readFile(sim, config); //parametros da simulacao

      // double incLoad = 150; //incremento da carga
      double incLoad = (Double) config.get(0); //incremento da carga
      System.out.println("inc Load = " + incLoad);

      //int points = 2; //numero de pontos (diferentes cargas de tréfego) a serem simulados
      int points = (Integer) config.get(1); //numero de pontos (diferentes cargas de tréfego) a serem simulados
      System.out.println("points = " + points);

      //int replyNumber = 2; //numero de replicações
      int replyNumber = (Integer) config.get(2); //numero de replicações
      System.out.println("reply number = " + replyNumber);

      //criando todas as simulações...
      /**
       * allSimulations é um Vector de Vector. Isto é, o 1º Vector armazena Vectors
       * com todas as replicações simuladas para uma mesma carga de tráfego.
       */
      Vector<Vector<Simulation>> allSimulations = new Vector<Vector<Simulation>> ();
      double newArriveRate = simulacao.getArrivedRate();
      //loop para geração de todos os pontos
      for (int i = 0; i < points; i++) {
        allSimulations.add(new Vector<Simulation> ());
        //loop para geração das replicações
        for (int j = 0; j < replyNumber; j++) {
          Simulation s = new Simulation(simulacao.getHoldRate(),
                                        newArriveRate,
                                        simulacao.getTotalNumberOfRequest(),
                                        simulacao.getSimulationType(),
                                        simulacao.getWAAlgorithm());
          s.setnumReply(j);

           Vector<Integer> conversionType=new Vector<Integer>(1);

           Mesh mesh = new Mesh(NodeFileController.readFile(net,conversionType)[0],
                             simulacao.getWAAlgorithm(), pairs,(Integer)config.get(3));
           mesh.setConversionType(conversionType.get(0));
           s.setMesh(mesh);
           allSimulations.lastElement().add(s);
        }
        newArriveRate += incLoad;
      }

      SimulationManagement management = new SimulationManagement(
          allSimulations);
      
      management.setSignificativeLevel((Double)config.get(4));
      management.setFailure((Boolean)config.get(5));
      management.setFixLinkRate((Double)config.get(6));
      management.setOccurRate((Double)config.get(7));
      
      management.setFileRes(res);      
      management.setGui(this);
      management.run(); 
    }
    
    public void progressRefresh(int percent) {
        this.jProgressBar.setValue(percent);
        this.jProgressBar.setString(percent+" %");
    }

    public void simFinished() {
       //JOptionPane.showMessageDialog(null,"Validação Concluída!","fim",JOptionPane.INFORMATION_MESSAGE);       
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton;
    private javax.swing.JPanel jPanel;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextArea jTextArea;
    // End of variables declaration//GEN-END:variables
    
}
