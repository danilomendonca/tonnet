package tools;

import java.io.*;
import java.util.Vector;
import simulator.*;
import network.Mesh;
import root.SimulationFileController;
import root.NodeFileController;
import measurement.*;
import root.Printer;

public class FLP {
  private String path;
  private int firstLoad;
  private Vector<NodeMeasure> results;
  private int replyForComputeFL;
  private int replyForSecStep;

  public FLP(String path) {
    this.path = path;
    this.results = new Vector<NodeMeasure> ();
    this.replyForComputeFL = 1;
    this.replyForSecStep = 10;
  }

  /**
   * Configura o numero de replicações
   * a serem feitas para calcular o FL
   * @param replyForComputeFL int
   */
  public void setReplyForComputeFL(int replyForComputeFL) {
    this.replyForComputeFL = replyForComputeFL;
  }

  /**
   * Configura o numero de replicações
   * a serem feitas na segunda etapa do algoritmo
   * @param replyForSecStep int
   */
  public void setReplyForSecStep(int replyForSecStep) {
    this.replyForSecStep = replyForSecStep;
  }

  /**
   * Executa o FLP em duas etapas
   * E1 - encontra o FL
   * E2 - Executa replicações com a carga FL
   * @param flMin int
   * @param flMax int
   * @param inc int
   * @param M int
   */
  public void run(int flMin, int flMax, int inc, float hurstMin, float hurstMax, int M) {
    //E1
    this.firstLoad = this.binarySearchFirstLoad(flMin, flMax, inc, M, hurstMin, hurstMax);

    //E2
    Vector<Measurements> measurements = new Vector<Measurements> ();
    measurements = this.runSimulation(firstLoad, hurstMin, hurstMax, this.replyForSecStep);

    Vector<NodeMeasure> nodeMList = measurements.get(0).getListNodeMeasure();
    //percorre os nós
    for (int i = 0; i < nodeMList.size(); i++) {
      //pecorre as replicações
      int sum = 0;
      for (int j = 0; j < this.replyForSecStep; j++) {
        sum += measurements.get(j).getListNodeMeasure().get(i).
            getMaximumWCUtilization();
      }
      //calcula/guarda a media da utilizacao maxima
      NodeMeasure aux = new NodeMeasure(nodeMList.get(i).getName());

      aux.setAverageWCUtilization( (double) sum / (double)this.replyForSecStep);
      this.results.add(aux);
    }

    int numWC = this.aproximate(this.results, M);
    this.completeWC(numWC, M);
  }
  //------------------------------------------------------------------------------
  /**
   * adiciona ou retira conversores
   * para ajustar o num de WC com o valor de M.
   * @param numWC int
   * @param M int
   */
  private void completeWC(int numWC, int M) {
    int diff = numWC - M;
    int numWC0 = this.ordenaByNumWC();
    // System.out.println("numWC0=" + numWC0);
    int index25 = (this.results.size() - numWC0) / 4; //indice maximo para completar 25%.
    //System.out.println("index25=" + index25);
    if (diff < 0) { //completar conversores
      int index = 0;
      for (int i = diff; i < 0; i++) {
        //System.out.println("i=" + i);
        double aux = this.results.get(index).getAverageWCUtilization();
        aux++;
        this.results.get(index).setAverageWCUtilization(aux);
        index++;
        if (index >= index25) {
          index = 0;
        }
      }
    }
    else { //retirar conversores
      int index = this.results.size() - numWC0 - 1;
      index25 = this.results.size() - numWC0 - index25;
      //  System.out.println("index25=" + index25);
      for (int i = diff; i > 0; i--) {
        //System.out.println("i=" + i);
        double aux = this.results.get(index).getAverageWCUtilization();
        aux--;
        results.get(index).setAverageWCUtilization(aux);
        if (aux == 0) {
          numWC0++;
        }
        index--;
        if (index < index25) {
          index = this.results.size() - numWC0 - 1;
          index25 = this.results.size() - numWC0 - (results.size() - numWC0) / 4;
        }
      }

    }
  }
  //------------------------------------------------------------------------------
  /**
   * Ordena pelo maior numero de WCs
   * @return int
   */
  private int ordenaByNumWC() {
    int numWC0 = 0;
    for (int i = 0; i < this.results.size(); i++) {
      for (int j = i + 1; j < this.results.size(); j++) {
        if (this.results.get(i).getAverageWCUtilization() <
            this.results.get(j).getAverageWCUtilization()) {
          NodeMeasure aux = this.results.get(i);
          this.results.set(i, this.results.get(j));
          this.results.set(j, aux);
        }
      }
      //System.out.println("i=" + i);
      if (this.results.get(i).getAverageWCUtilization() == 0) {
        // System.out.println("no=" + results.get(i).getName());
        numWC0++;
      }
    }
    return numWC0;
  }

  /**
   * Monta array de cargas
   * @param min int carga min
   * @param max int carga max
   * @param inc int incremento da carga
   * @return int[] array
   */
  private int[] montaLoadArray(int min, int max, int inc) {
    int length = 0;
    for (int i = min; i <= max; i += inc) {
      length++;
    }

    int[] array = new int[length];

    int value = min;
    for (int i = 0; i < length; i++) {
      array[i] = value;
      value += inc;
    }

    return array;
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna valor de carga cujo pico da qtd de uso de conversores fica mais proximo de M
   * @param flMin int carga min
   * @param flMax int carga max
   * @param inc int incremento da carga
   * @param M int M
   * @return int carga
   */
  private int binarySearchFirstLoad(int flMin, int flMax, int inc, int M, float hurstMin, float hurstMax) {
    int loadArray[] = this.montaLoadArray(flMin, flMax, inc);
    double valueArray[] = new double[loadArray.length];

    int esq = 0;
    int dir = loadArray.length - 1;
    int indiceMeio;

    while (esq <= dir) {
      System.out.println("[" + loadArray[esq] + "," + loadArray[dir] + "]");
      indiceMeio = (esq + dir) / 2;
      System.out.println("pivo=" + loadArray[indiceMeio]);
      //simula e retorna pico para a carga i
      valueArray[indiceMeio] = computeValueN(loadArray[indiceMeio], hurstMin, hurstMax);
      System.out.println("somaWcMax=" + valueArray[indiceMeio]);
      if (valueArray[indiceMeio] == M) {
        return loadArray[indiceMeio];
      }
      if (valueArray[indiceMeio] < M) {
        esq = indiceMeio + 1;
      }
      else if (valueArray[indiceMeio] > M) {
        dir = indiceMeio - 1;
      }
    }

    //Nao foi encontrado o valor exato..
    if (esq >= valueArray.length) {
      esq = valueArray.length - 1;
    }
    if (dir < 0) {
      dir = 0;
    }
    // caso termine com indice esq>dir...troca indices
    if (esq > dir) {
      int aux = dir;
      dir = esq;
      esq = aux;
    }

    double deltaEsq = M - valueArray[esq];
    double deltaDir = valueArray[dir] - M;

//verifica se o M esta mais proximo do valor da carga a esquerda ou a direita
    if (deltaEsq < deltaDir) {
      return loadArray[esq];
    }
    return loadArray[dir];
  }
  //------------------------------------------------------------------------------
  /**
   * simula e retorna pico N para a carga newArriveRate
   * @param arriveRate int
   * @return int
   */
  private double computeValueN(int arriveRate, float hurstMin, float hurstMax) {
    Vector<Measurements> m = new Vector<Measurements> ();
    m = this.runSimulation(arriveRate, hurstMin, hurstMax, this.replyForComputeFL);

    int sum = 0;
    for (int i = 0; i < this.replyForComputeFL; i++) {
      int sumReply = m.get(i).getSumMaximumWCUtilization();
      sum += sumReply;
    }

    return sum / this.replyForComputeFL;
  }
  //------------------------------------------------------------------------------
  /**
   * Executa uma simulação para uma determinada carga
   * Retorna um Vector<Measurements>
   * @param arriveRate int
   * @param replyNumber int
   * @return Vector Measurements
   */
  private Vector<Measurements> runSimulation(int arriveRate, float hurstMin, float hurstMax, int replyNumber) {

    String net = "files/" + this.path + "/network.net";
    String sim = "files/" + this.path + "/simulation.sim";
    String pairs = "files/" + this.path + "/pairs.prs";

    Vector config = new Vector(3);
    Simulation simulacao = SimulationFileController.readFile(sim, config); //parametros da simulacao

    /**
     * allSimulations é um Vector de Vector. Isto é, o 1º Vector armazena Vectors
     * com todas as replicações simuladas para uma mesma carga de tráfego.
     */
    Vector<Vector<Simulation>> allSimulations = new Vector<Vector<Simulation>> ();

    allSimulations.add(new Vector<Simulation> ());

    //loop para geração das replicações
    for (int j = 0; j < replyNumber; j++) {
      Simulation s = new Simulation(simulacao.getHoldRate(),
                                    arriveRate,
                                    hurstMin,
                                    hurstMax,
                                    simulacao.getTotalNumberOfRequest(),
                                    simulacao.getSimulationType(),
                                    simulacao.getWAAlgorithm());
      s.setnumReply(j);

      Vector<Integer> conversionType = new Vector<Integer> (1);

      Mesh mesh = new Mesh(NodeFileController.readFile(net, conversionType)[0],
                           simulacao.getWAAlgorithm(), pairs, 0, 0);//TODO verificar último parâmetro de tipo de tráfego
      mesh.setConversionType(conversionType.get(0));
      s.setMesh(mesh);
      allSimulations.lastElement().add(s);
    }
    SimulationManagement management = new SimulationManagement(
        allSimulations);
    
      management.setSignificativeLevel((Double)config.get(4));
      management.setFailure((Boolean)config.get(5));
      management.setFixLinkRate((Double)config.get(6));
      management.setOccurRate((Double)config.get(7));
      management.start();    
      
    return management.getMeasurements().get(0);
  }
  //------------------------------------------------------------------------------
  /**
   * Aproxima os valores de double para inteiro
   * @param results Vector
   * @param M int
   * @return int
   */
  private int aproximate(Vector<NodeMeasure> results, int M) {
    int sum = 0;
    for (int i = 0; i < results.size(); i++) {
      Double doubleAverage = ( (Double) results.get(i).getAverageWCUtilization());
      int intAverage = doubleAverage.intValue();
      if ( (doubleAverage - (double) intAverage) > 0.5) {
        intAverage++;
      }
      results.get(i).setAverageWCUtilization(intAverage);
      sum += intAverage;
    }
    return sum;
  }
  /**
   * Exibe os resultados na console e salva-os em arquivo
   */
  public void printResults() {
    System.out.println("### Resultado FLP ###");
    double sum = 0;

    Printer fileOut = null;
    try {
      fileOut = new Printer("files/" +this.path + "/flpResult.res",true);
    }
    catch (FileNotFoundException ex) {
    }

    fileOut.println("FL\t" + this.firstLoad);
    fileOut.println("Node\tWCs");

    for (int i = 0; i < this.results.size(); i++) {
      NodeMeasure aux = this.results.get(i);
      sum += aux.getAverageWCUtilization();
      fileOut.println(aux.getName() + "\t" +
                      aux.getAverageWCUtilization());
    }

    fileOut.println("Total\t" + sum);
    fileOut.closeFile();

  }
}
