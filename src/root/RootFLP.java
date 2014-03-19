package root;

import java.io.FileNotFoundException;
import java.util.Vector;
import tools.FLP;

public class RootFLP {

  public static void main(String[] args) throws FileNotFoundException {
    FLP flp = new FLP(args[0]);
    Vector config = FlpFileController.readFile("files/" + args[0] + "/flp.flp");
    int flMin = (Integer) config.get(0);
    int flMax = (Integer) config.get(1);
    int inc = (Integer) config.get(2);
    float hurstMin = (Float) config.get(3);
    float hurstMax = (Float) config.get(4);
    int M = (Integer) config.get(5);

    flp.setReplyForComputeFL(10);
    flp.setReplyForSecStep(10);

    flp.run(flMin, flMax, inc, hurstMin, hurstMax, M);
    flp.printResults();
  }
}
