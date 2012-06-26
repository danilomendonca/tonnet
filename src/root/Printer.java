package root;

import java.io.*;
import java.util.Vector;
import network.*;

public class Printer {

  private PrintWriter pW;
  private boolean console;

  /**
   * Creates a new instances of Printer.
   * @param nameFile String
   * @throws FileNotFoundException
   */
  public Printer(String nameFile, boolean console) throws FileNotFoundException {
    this.pW = new PrintWriter(new FileOutputStream(nameFile));
    this.console = console;
  }

  //------------------------------------------------------------------------------
  /**
   * PrintLn Path in file.
   * @param path Vector
   */
  public void printlnPath(Vector<Node> path) {
    for (int i = 0; i < path.size(); i++) {
      String s = path.get(i).getName() + ";";
      this.pW.print(s);
    }
    this.pW.println();
  }

//------------------------------------------------------------------------------
  /**
   * print content in file
   * @param content String
   */
  public void print(String content) {
    if (console) {
      System.out.print(content);
    }
    this.pW.print(content);
  }

//------------------------------------------------------------------------------
  /**
   * PrintLn content in File
   * @param content String
   */
  public void println(String content) {
    if (console) {
      System.out.println(content);
    }
    this.pW.println(content);
  }

  //------------------------------------------------------------------------------
  /**
   * PrintLn content in File
   * @param content String
   */
  public void println() {
    if (console) {
      System.out.println();
    }
    this.pW.println("");
  }

//------------------------------------------------------------------------------
  /**
   * Close file
   */
  public void closeFile() {
    this.pW.flush();
    this.pW.close();
  }
}
