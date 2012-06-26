package wavelengthAssignment;

import network.*;

public abstract class WaveAlgo {
  protected Mesh mesh;
  /**
   * M�todo invocado pelas requisi��es
   * Dado uma lista de Comprimentos de onda livres continuos,
   * seta em waveList quais os comprimentos de onda serao utilizados
   * @param waveList int[] comprimentos de onda serao utilizados
   * @param freeWave int[] lista de comprimentos de onda livres
   * @param category int categoria do Par
   * @return boolean. True se a configura��o foi feita com sucesso.
   */
  public abstract boolean setWaveAssignment(int[] waveList, int[] freeWave,
                                            int category);

  public void setMesh(Mesh m) {
    this.mesh = m;
  }

}
