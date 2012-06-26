package root;

import java.io.FileNotFoundException;
import tools.TOT;

public class RootTOT {

  public static void main(String[] args) throws FileNotFoundException {

    TOT tot = new TOT(args[0]);

    tot.run();
    tot.printResults();
  }

}
