package wavelengthAssignment;

public class Random
    extends WaveAlgo {

  /**
   * método invocado pelas requisições
   *Dado uma lista de comprimentos de onda livres Comprimentos de onda livres continuos.cenario sem conversao
   *Seta em waveList quais os comprimentos de onda serao utilizados
   * @param waveList int[] comprimentos de onda serao utilizados
   * @param freeWave int[] lista de comprimentos de onda livres
   * @param category int categoria do par
   * @return boolean
   */
  public boolean setWaveAssignment(int[]waveList, int[] freeWave, int category) {
    int index;
    index = this.mesh.getRandomVar().randInt(0, freeWave.length - 1);

    for (int i = 0; i < waveList.length; i++) {
      waveList[i]= freeWave[index];
    }
    return true;
  }
}
