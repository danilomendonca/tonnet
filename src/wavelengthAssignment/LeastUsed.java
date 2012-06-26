package wavelengthAssignment;

import network.*;

public class LeastUsed
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
  public boolean setWaveAssignment(int[] waveList, int[] freeWave, int category) {
    int index = 0;

    //utilização de cada comprimento de onda
    double[] wavelengthUtilization = this.mesh.calculateWavelengthUtilization();
    //escolhendo da lista de comprimento de onda livre o comprimento de onda menos utilizado na rede
    for (int i = 1; i < freeWave.length; i++) {
      if (wavelengthUtilization[freeWave[i]] <
          wavelengthUtilization[freeWave[index]]) {
        index = i;
      }
    }

    //seta o comprimento de onda a ser utilizado por uma requisição
    for (int i = 0; i < waveList.length; i++) {
      waveList[i] = freeWave[index];
    }

    return true;
  }
}
