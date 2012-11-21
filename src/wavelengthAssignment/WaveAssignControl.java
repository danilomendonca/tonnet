package wavelengthAssignment;

import network.*;

public class WaveAssignControl {
  private WaveAlgo algorithm;

  public WaveAssignControl(Mesh m, String wAAlgorithm) {
    if (wAAlgorithm.equalsIgnoreCase("Random")) {
      algorithm = new Random();
    }
    else if ((wAAlgorithm.equalsIgnoreCase("First Fit"))||(wAAlgorithm.equalsIgnoreCase("FirstFit"))) {
      algorithm = new FirstFit();
    }
    else if (wAAlgorithm.equalsIgnoreCase("Most Used")) {
      algorithm = new MostUsed();
    }
    else if (wAAlgorithm.equalsIgnoreCase("Least Used")) {
      algorithm = new LeastUsed();
    }
    else if (wAAlgorithm.equalsIgnoreCase("Fair Fit")) {
      algorithm = new FairFit();
    }
    else if (wAAlgorithm.equalsIgnoreCase("Max Sum")) {
      algorithm = new MaxSum();
    }
    else if (wAAlgorithm.equalsIgnoreCase("RCL")) {
        algorithm = new RCL();
     }

    algorithm.setMesh(m);
  }

  /**
   * Define quais os comprimentos de onda que serão utilizados nos enlaces da
   * rota.
   * @param wAAlgorithm String
   * @param r Route
   * @param int category
   * @param freeWave int[]
   */
  public boolean run(int[] waveList, int[] freeWave,int category) {
    return algorithm.setWaveAssignment(waveList, freeWave, category);
  }

}
