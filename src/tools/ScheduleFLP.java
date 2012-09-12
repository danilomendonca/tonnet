package tools;

import java.io.*;
import root.RootFLP;

public class ScheduleFLP {
  public ScheduleFLP() {
  }

  /**
   * Permite o agendamento de varias simulações FLP.
   * Cada parametro um path de uma simulação
   * @param args String[]
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void main(String[] args) throws FileNotFoundException,
      IOException {
    String[] simulations = new String[50];
    String tmp = "files/" + args[0];
    FileReader file = new FileReader(tmp);
    BufferedReader in = new BufferedReader(file);

    int index = -1;
    while (in.ready()) {
      index++;
      simulations[index] = in.readLine();
    }

    String[] argsAux = new String[1];
    System.out.println("...... AGENDA ......");
    for (int i = 0; (i < simulations.length) && (simulations[i]!=null); i++) {
      System.out.println("######### Simulacao FLP " + (i + 1) + " de " + (index+1) +
                             " #########");
        argsAux[0] = simulations[i];
        RootFLP.main(argsAux);
      }

  }
}
