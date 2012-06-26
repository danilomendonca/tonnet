package tools;

import root.*;
import measurement.*;
import simulator.*;
import network.Mesh;
import java.io.FileNotFoundException;
import java.util.Vector;

public class TOT {
  private String path;
  private Vector<NodeMeasure> results;

  public TOT(String path) {
    this.path = path;
    results = new Vector<NodeMeasure> ();
  }

  /**
   * Executa a t�cnica TOT - Total Outorsing Traffic
   * Constitue em aplicar a quantidade m�xima de convers�o
   * � aqueles n�s que tem mais rotas que o atravessam
   */
  public void run() {
    Vector<Measurements> measurements = this.runSimulation();

    this.results = measurements.get(0).getListNodeMeasure();
    this.ordenaByNumRoutesCrossing();
  }

  //------------------------------------------------------------------------------
  /**
   * Ordena pelo maior numero de rotas que passam pelo n�
   */
  private void ordenaByNumRoutesCrossing() {
    for (int i = 0; i < results.size() - 1; i++) {
      for (int j = i + 1; j < results.size(); j++) {
        if (results.get(i).getNumRoutesCrossing() <
            results.get(j).getNumRoutesCrossing()) {
          NodeMeasure aux = results.get(i);
          results.set(i, results.get(j));
          results.set(j, aux);
        }
      }
    }
  }

//------------------------------------------------------------------------------
  /**
   * Executa a simula��o
   * Retorna um Vector<Measurements>
   * @return Vector Measurements
   */
  private Vector<Measurements> runSimulation() {

    String net = "files/" + this.path + "/network.net";
    String sim = "files/" + this.path + "/simulation.sim";
//    String res = "files/" + this.path + "/results.res";
    String pairs = "files/" + this.path + "/pairs.prs";

    Vector config = new Vector(3);
    Simulation simulacao = SimulationFileController.readFile(sim, config); //parametros da simulacao

    /**
     * allSimulations � um Vector de Vector. Isto �, o 1� Vector armazena Vectors
     * com todas as replica��es simuladas para uma mesma carga de tr�fego.
     */
    Vector<Vector<Simulation>> allSimulations = new Vector<Vector<Simulation>> ();

    double incLoad = (Double) config.get(0); //incremento da carga
    int points = (Integer) config.get(1);
    int replyNumber = (Integer) config.get(2); //numero de replica��es
    double newArriveRate = simulacao.getArrivedRate();
    //loop para gera��o de todos os pontos
    for (int i = 0; i < points; i++) {
      allSimulations.add(new Vector<Simulation> ());
      //loop para gera��o das replica��es
      for (int j = 0; j < replyNumber; j++) {
        Simulation s = new Simulation(simulacao.getHoldRate(),
                                      newArriveRate,
                                      simulacao.getTotalNumberOfRequest(),
                                      simulacao.getSimulationType(),
                                      simulacao.getWAAlgorithm());
        s.setnumReply(j);

        Vector<Integer> conversionType = new Vector<Integer> (1);

        Mesh mesh = new Mesh(NodeFileController.readFile(net, conversionType)[0],
                             simulacao.getWAAlgorithm(), pairs, 0);
        mesh.setConversionType(conversionType.get(0));
        s.setMesh(mesh);
        allSimulations.lastElement().add(s);
      }
      newArriveRate += incLoad;
    }
    SimulationManagement management = new SimulationManagement(
        allSimulations);
    management.setSignificativeLevel( (Double) config.get(4));
    
      management.setFailure((Boolean)config.get(5));
      management.setFixLinkRate((Double)config.get(6));
      management.setOccurRate((Double)config.get(7));
      management.start();     
    /* try {
     management.printResults(res);
     }
     catch (FileNotFoundException ex) {
         }*/
    return management.getMeasurements().get(0);
  }

//------------------------------------------------------------------------------
  /**
   * Exibe os resultados na console e salva-os em arquivo
   */
  public void printResults() {
    System.out.println("### Resultado TOT ###");

    Printer fileOut = null;
    try {
      fileOut = new Printer("files/" + this.path + "/totResult.res", true);
    }
    catch (FileNotFoundException ex) {
    }

    fileOut.println("Num.Routes\tNode\tWCs\tM");
    int sum = 0;
    for (int i = 0; i < this.results.size(); i++) {
      NodeMeasure aux = this.results.get(i);
      sum += aux.getNumMaxWC();
      fileOut.println(aux.getNumRoutesCrossing() + "\t" + aux.getName() + "\t" +
                      aux.getNumMaxWC() + "\t" + sum);
    }

    fileOut.closeFile();

  }
}
