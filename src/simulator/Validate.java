package simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.Vector;

public class Validate {
  private Validate() {
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna true se as medias do arquivo fileOkname(corretas) estão
   * dentro do intervalo de confianca fornecido pelo arquivo "file2Name"
   * @param fileOKname String
   * @param file2Name String
   * @return boolean
   * @throws Exception
   */
  public static boolean validate(String fileOKname, String file2Name) throws
      Exception {
    //armazena as medias do arquivo OK
    Vector<Double> averagesOK = new Vector<Double> ();
    //armazena as medias e os respectivos erros do arquivo a ser validado
    Vector<Vector<Double>> averagesAndErrors = new Vector<Vector<Double>> (2);

    //FILE OK
    averagesOK = Validate.getAverages(fileOKname);

    //FILE 2
    averagesAndErrors = Validate.getAveragesAndErrors(file2Name);

    return Validate.compare(averagesOK, averagesAndErrors);
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna um Vector de medias
   * @param fileName String url do arquivo com os resultados
   * @return Vector
   * @throws Exception
   */
  private static Vector<Double> getAverages(String fileName) throws Exception {
    BufferedReader in;
    FileReader file = new FileReader(fileName);
    in = new BufferedReader(file);
    Vector<Double> averages = new Vector<Double> ();
    String endDelim = "";
    while (! (endDelim.equalsIgnoreCase("outras..."))) {
      //descartando primeira linha (nome da métrica)
      in.readLine();
      //linha 1 computando a quantidade de delimitadores ate chegar a media
      StringTokenizer line = new StringTokenizer(in.readLine(), ";\t");
      int contDelim = 0;
      while (line.hasMoreElements()) {
        String token = line.nextToken();
        if (token.equalsIgnoreCase("average")) {
          break;
        }
        else {
          contDelim++;
        }
      }

      line = new StringTokenizer(in.readLine(), ";\t");
      //verifica se existe mais medias(cargas)
      while (line.countTokens() > 1) {
        int cont = 0;
        //"pulando" a quantidade certa de tokens desnecessários
        while ( (line.hasMoreElements()) && (cont != contDelim)) {
          line.nextToken();
          cont++;
        }
        //guardando a media
        double average = Double.valueOf(line.nextToken());
        averages.add(average);
        line = new StringTokenizer(in.readLine(), ";\t");
      }
      if (line.hasMoreElements()) {
        endDelim = line.nextToken();
      }
      //   System.out.println("end?=" + endDelim);

    }
    return averages;
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna um Vector<Vector> com 2 posições:
   * 1->medias
   * 2->erros respectivos
   * @param fileName String url do arquivo com os resultados
   * @return Vector
   * @throws Exception
   */
  private static Vector<Vector<Double>> getAveragesAndErrors(String fileName) throws
      Exception {
    BufferedReader in;
    FileReader file = new FileReader(fileName);
    in = new BufferedReader(file);
    Vector<Vector<Double>> averagesAndErrors = new Vector<Vector<Double>> (2);
    averagesAndErrors.add(new Vector());
    averagesAndErrors.add(new Vector());
    String endDelim = "";
    while (! (endDelim.equalsIgnoreCase("outras..."))) {
      //descartando primeira linha (nome da métrica)
      in.readLine();
      //linha 1 computando a quantidade de delimitadores ate chegar a media
      StringTokenizer line = new StringTokenizer(in.readLine(), ";\t");
      int contDelim = 0;
      while (line.hasMoreElements()) {
        String token = line.nextToken();
        if (token.equalsIgnoreCase("average")) {
          break;
        }
        else {
          contDelim++;
        }
      }

      line = new StringTokenizer(in.readLine(), ";\t");
      //verifica se existe mais medias(cargas)
      while (line.countTokens() > 1) {
        int cont = 0;
        //"pulando" a quantidade certa de tokens desnecessários
        while ( (line.hasMoreElements()) && (cont != contDelim)) {
          line.nextToken();
          cont++;
        }
        //guardando a media
        double average = Double.valueOf(line.nextToken());
        double error = Double.valueOf(line.nextToken());
        averagesAndErrors.get(0).add(average);
        averagesAndErrors.get(1).add(error);
        line = new StringTokenizer(in.readLine(), ";\t");
      }
      if (line.hasMoreElements()) {
        endDelim = line.nextToken();
      }
      //  System.out.println("end?=" + endDelim);

    }

    return averagesAndErrors;
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna true se as medias corretas estão dentro do intervalo de confianca
   * das medias a serem validadas
   * @param averagesOK Vector medias corretas
   * @param averagesAndErrors Vector medias com respectivos erros
   * @return boolean
   */
  private static boolean compare(Vector<Double> averagesOK,
      Vector<Vector<Double>> averagesAndErrors) {
    boolean flag = true;
    for (int i = 0; i < averagesOK.size(); i++) {
      //média ok
      double averageOK = averagesOK.get(i);
      //média 2
      double average2 = averagesAndErrors.get(0).get(i);
      //lim inferior
      double limInf = average2 - averagesAndErrors.get(1).get(i);
      //lim superior
      double limSup = average2 + averagesAndErrors.get(1).get(i);

      //verifica se esta no intervalo
      double ajust = 0.01; //ajuste
      if ( (averageOK < limInf - ajust) || (averageOK > limSup + ajust)) {
        System.err.print(averageOK + " i :" + i + " -> ");
        System.err.println(average2 + " i :" + i);
        flag = false;
      }
    }

    return flag;
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna os nomes principais dos arquivos de validacao
   * @return Vector com os nomes.
   */
  public static Vector<String> getFiles() {
    Vector<String> files = new Vector<String> ();

      //############## Dissertação de mestrado Andre Soares ##############\\
      String path = "DissertacaoAndreSoares/";
      //Fixo,Anel 20 nós
   files.add(path + "SoaresAnelUniN20RD");
      files.add(path + "SoaresAnelBidN20RD");
     // Fixo, Arpanet
      files.add(path + "SoaresFixoArpanFC");
     files.add(path + "SoaresFixoArpanFF");
      files.add(path + "SoaresFixoArpanRD");

      //############## Sbrt 2005 ##############\\
     path = "sbrt2005/";
   //Fixo, Arpanet
      files.add(path + "sbrtFixoArpanFC");
      files.add(path + "sbrtFixoArpanFF");
      files.add(path + "sbrtFixoArpanRD");
      //Two Step, Arpanet
      files.add(path + "sbrtTwoStepSbArpanFC");
      files.add(path + "sbrtTwoStepSbArpanFF");
      files.add(path + "sbrtTwoStepSbArpanRD");
      files.add(path + "sbrtTwoStepCbArpanFC");
      files.add(path + "sbrtTwoStepCbArpanFF");
      files.add(path + "sbrtTwoStepCbArpanRD");
     //Fixo, Abilene
      files.add(path + "sbrtFixoAbileFC");
      files.add(path + "sbrtFixoAbileFF");
      files.add(path + "sbrtFixoAbileRD");

      //Two Step, Abilene
      files.add(path + "sbrtTwoStepSbAbileFF");
      files.add(path + "sbrtTwoStepSbAbileRD");
      files.add(path + "sbrtTwoStepCbAbileFC");
      files.add(path + "sbrtTwoStepCbAbileFF");
      files.add(path + "sbrtTwoStepCbAbileRD");

      //Two Step,SimRWA, Zhang, 2003 - "on the study of routing and wavelength assignment" Fig 5.b pg21
      files.add("simRWATwoStepSbZhangFF");
      //############## Sbrc 2006 ##############\\
      path = "sbrc2006/";
      //FLP
      files.add(path + "sbrcFixoFlpNsfnetSP");
      files.add(path + "sbrcFixoNsfnetFC");
      files.add(path + "sbrcFixoNsfnetFF");
      //...
     return files;
  }
}
