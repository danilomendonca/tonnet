package root;

import java.io.*;
import java.util.*;

public class FlpFileController {

  public static Vector readFile(String fileName) {
    Integer flMin=1;
    Integer flMax=10;
    Integer inc=1;
    Integer M=1;
    BufferedReader in;
    FileReader file;

    try {
      file = new FileReader(fileName);
      in = new BufferedReader(file);
      //LINE 1-lendo os comentarios
      String comment = in.readLine();
      System.out.println(comment);
      StringTokenizer line = new StringTokenizer(in.readLine(), ";\t");

      //LINE 2-lendo as configurações
      flMin = Integer.valueOf(line.nextToken());
      flMax = Integer.valueOf(line.nextToken());
      inc = Integer.valueOf(line.nextToken());
      M = Integer.valueOf(line.nextToken());

    }
    catch (IOException e) {
      System.err.println("File Error - Sintaxe or file not found");
    }

    Vector config = new Vector();
    config.add(flMin);
    config.add(flMax);
    config.add(inc);
    config.add(M);
    return config;
  }

}
