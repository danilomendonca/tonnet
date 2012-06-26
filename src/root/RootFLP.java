package root;

import tools.FLP;
import java.util.Vector;
import java.io.FileNotFoundException;

public class RootFLP {

  public static void main(String[] args) throws FileNotFoundException {
    FLP flp = new FLP(args[0]);
    Vector config = FlpFileController.readFile("files/" + args[0] + "/flp.flp");
    int flMin = (Integer) config.get(0);
    int flMax = (Integer) config.get(1);
    int inc = (Integer) config.get(2);
    int M = (Integer) config.get(3);

    flp.setReplyForComputeFL(10);
    flp.setReplyForSecStep(10);

    flp.run(flMin, flMax, inc, M);
    flp.printResults();
  }
}
