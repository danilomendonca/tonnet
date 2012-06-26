package root;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import simulator.Simulation;

public class SimulationFileController {

  public static Simulation readFile(String fileName, Vector config) {
    double holdRate = 1;
    double arrivedRate = 1;
    int numberReq = 1;
    int simulationType = 1;
    String wAssing = "";

    BufferedReader in;
    FileReader file;

    try {
      file = new FileReader(fileName);
      in = new BufferedReader(file);

      //LINE 1-lendo os comentarios
      String comment = in.readLine();
      System.out.println(comment);

      //LINE 2-lendo os parametros principais
      StringTokenizer line = new StringTokenizer(in.readLine(), ";\t");

      holdRate = Double.parseDouble(line.nextToken());
      arrivedRate = Double.parseDouble(line.nextToken());
      numberReq = Integer.valueOf(line.nextToken());
      simulationType = Integer.valueOf(line.nextToken());
      wAssing = line.nextToken();

      //LINE 3-lendo incremento da carga, num pontos e replicas
      line = new StringTokenizer(in.readLine(), ";\t");
      //carga
      config.add(Double.parseDouble(line.nextToken()));
      //pontos
      config.add(Integer.valueOf(line.nextToken()));
      //replicações
      config.add(Integer.valueOf(line.nextToken()));

      //LINE 3-tipo de trafego
      line = new StringTokenizer(in.readLine(), ";\t");
      config.add(Integer.valueOf(line.nextToken()));

      //LINE 5-nivel de confiança
      line = new StringTokenizer(in.readLine(), ";\t");
      config.add(Double.parseDouble(line.nextToken()));
      line = new StringTokenizer(in.readLine(), ";\t");
      //LINE 6-Verifica se a simulação vai gerar falha
      if ((line.nextToken()).equalsIgnoreCase("true")) {
        config.add(new Boolean(true));
        //LinkRate
        config.add(Double.parseDouble(line.nextToken()));
        //occurRate
        config.add(Double.parseDouble(line.nextToken()));
      }else{
        config.add(new Boolean(false));
        config.add(new Double(0));
        config.add(new Double(0));
      }
    }
    catch (IOException e) {
      System.err.println("File Error - Sintaxe or file not found");
    }
    System.out.println("holdRate = " + holdRate);
    System.out.println("arrivedRate = " + arrivedRate);
    System.out.println("numberReq = " + numberReq);
    System.out.println("simulationType = " + simulationType);
    System.out.println("wAssing = " + wAssing);
    return new Simulation(holdRate, arrivedRate, numberReq, simulationType,
                          wAssing);
  }

}
