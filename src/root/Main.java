package root;

import network.*;
import java.util.*;
import java.io.FileNotFoundException;
import simulator.*;

public class Main {
  /**
   * Se n�o passar par�metro nenhum e feita a valida��o
   * @param args String[] diretorio que contem os arquivos para simula��o
   * @throws FileNotFoundException
   */
  public static void main(String args[]) throws FileNotFoundException {

    if (args.length==0) { //auto-valida��o
      System.out.println("Validate...");
      try {
        //armazena os nomes principais dos arquivos de validacao
        Vector<String> files = Validate.getFiles();
        //percorre a lista de arquivos e inicia as respectivas simulacoes e validacoes
        for (int i = 0; i < files.size(); i++) {
          System.out.println("######### Etapa " + (i + 1) + " de " + files.size() +
                             " #########");
          String validateArgs[] = new String[4];
          validateArgs[0] = "validate/" + files.get(i);
          //executa a simulacao de validacao i
          Main.main(validateArgs);
          //compara os resultados da simulacao i com os respectivos resultados "validos"
          boolean valid = Validate.validate("files/validate/" + files.get(i) +
                                            "/resultsOk.res",
                                            "files/validate/" + files.get(i) +
                                            "/results.res");
          System.out.println("validate result " + (i + 1) + " : " + valid);
          System.out.println("------------------------------------------------");
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        System.err.println("Validate Error");
      }
    }
    else { //-------------------------------------------------------------------
      String net = "files/" + args[0] + "/network.net";
      String sim = "files/" + args[0] + "/simulation.sim";
      String res = "files/" + args[0] + "/results.res";
      String pairs = "files/" + args[0] +"/pairs.prs";

      Vector config = new Vector(3);
      Simulation simulacao = SimulationFileController.readFile(sim, config); //parametros da simulacao

      // double incLoad = 150; //incremento da carga
      double incLoad = (Double) config.get(0); //incremento da carga
      System.out.println("inc Load = " + incLoad);

      //int points = 2; //numero de pontos (diferentes cargas de tr�fego) a serem simulados
      int points = (Integer) config.get(1); //numero de pontos (diferentes cargas de tr�fego) a serem simulados
      System.out.println("points = " + points);

      //int replyNumber = 2; //numero de replica��es
      int replyNumber = (Integer) config.get(2); //numero de replica��es
      System.out.println("reply number = " + replyNumber);

      //criando todas as simula��es...
      /**
       * allSimulations � um Vector de Vector. Isto �, o 1� Vector armazena Vectors
       * com todas as replica��es simuladas para uma mesma carga de tr�fego.
       */
      Vector<Vector<Simulation>> allSimulations = new Vector<Vector<Simulation>> ();
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
      
      management.start();         
    }
  }
}
